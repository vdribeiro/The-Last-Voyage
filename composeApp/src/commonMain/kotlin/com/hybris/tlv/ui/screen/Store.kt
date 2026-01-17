package com.hybris.tlv.ui.screen

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hybris.tlv.domain.command.Command
import com.hybris.tlv.domain.command.sendCommand
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Screen

/**
 * The central hub for a screen's [State]. It's the single source of truth for the UI.
 * It receives [Action]s from the UI and calls the appropriate Use Cases to handle the business logic for that action.
 * After it receives the result from the Use Case, it combines it with the current [State], and emits a new [State].
 * A key rule is that the UI only observes the Store's [State] and never modifies it directly.
 */
internal open class Store<State, Action>(initialState: State): ViewModel() {

    /**
     * The current state of the screen.
     */
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    /**
     * Active jobs launched by the Store.
     */
    private val activeJobs = mutableMapOf<String, Job>()

    /**
     * Sends an [Action] to the Store.
     */
    fun send(action: Action) = reducer(state = _stateFlow.value, action = action)

    /**
     * Called when an [Action] is sent to the Store.
     * It uses the current [state] and the [action] to produce a new [State].
     */
    protected open fun reducer(state: State, action: Action) {}

    /**
     * Updates the current [State].
     */
    protected fun updateState(body: (State) -> State): State =
        _stateFlow.updateAndGet(function = body)

    /**
     * Launches a [Job] returned by [block] given an unique identifier [id] and [replace] parameter.
     * If [id] is null, then the job is launched in 'fire and forget' mode.
     * If [replace] is true and a job with [id] is already active, the existing job will be cancelled and replaced by the new one, otherwise the new request is ignored and the existing job is returned.
     */
    private fun launchJob(
        id: String?,
        replace: Boolean,
        block: () -> Job
    ): Job = if (id == null) block() else {
        val job = activeJobs[id]?.takeIf { it.isActive }
        if (job != null && !replace) job else {
            job?.cancel()
            block().also { job ->
                activeJobs[id] = job
                job.invokeOnCompletion { if (activeJobs[id] === job) activeJobs.remove(key = id) }
            }
        }
    }

    /**
     * Launches a coroutine given an optional unique identifier [id].
     * If [id] is provided, it ensures only one job with this id runs, otherwise the job is launched in 'fire and forget' mode.
     * If [replace] is true and a job with [id] is already active, the existing job will be cancelled and replaced by the new one, otherwise the new request is ignored and the existing job is returned.
     */
    protected fun launch(
        id: String? = null,
        replace: Boolean = false,
        context: CoroutineContext = Dispatcher.Default,
        block: suspend CoroutineScope.() -> Unit
    ): Job = launchJob(
        id = id,
        replace = replace,
    ) { viewModelScope.launch(context = context, block = block) }

    /**
     * Collects the upstream [kotlinx.coroutines.flow.Flow] in a lifecycle-aware manner, ensuring execution only occurs while the UI is actively observing the [stateFlow].
     * If [id] is provided, it ensures only one job with this id runs.
     * If [replace] is true and a job with [id] is already active, the existing job will be cancelled and replaced by the new one, otherwise the new request is ignored and the existing job is returned.
     * This function also acts as a resource safeguard, bridging the gap between the [ViewModel] scope which can persist in the backstack and the UI lifecycle which pauses when hidden.
     * The calling flow runs in the provided [context] and a [timeout] in milliseconds is used as a grace period to wait after the last subscriber disappears before cancelling the upstream flow.
     * The reason for this is to keep the connection alive when the subscription count drops to zero temporarily (screen rotation, configuration changes, rapid navigation, etc...),
     * preventing the flow from restarting unnecessarily.
     * The suspending lambda [block] is executed on the [viewModelScope] whenever the upstream flow emits a value.
     * Finally, a [Job] is retuned representing the active collection logic, scoped to the [viewModelScope].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <T> Flow<T>.observe(
        id: String? = null,
        replace: Boolean = false,
        context: CoroutineContext = Dispatcher.IO,
        timeout: Long = 5000L,
        block: suspend (T) -> Unit
    ): Job = launchJob(
        id = id,
        replace = replace,
    ) {
        _stateFlow.subscriptionCount
            .map { count -> count > 0 }
            .distinctUntilChanged()
            .transformLatest { isVisible ->
                if (isVisible) emit(value = true) else {
                    delay(timeMillis = timeout)
                    emit(value = false)
                }
            }
            .flatMapLatest { isVisible -> if (isVisible) flowOn(context = context) else emptyFlow() }
            .onEach { data -> block(data) }
            .launchIn(scope = viewModelScope)
    }

    /**
     * Navigate back.
     */
    fun back() = back(state = _stateFlow.value)

    /**
     * Overridable back navigation.
     */
    protected open fun back(state: State) {
        sendCommand(command = Command.Back)
    }

    /**
     * Navigate to a new [screen].
     */
    protected fun navigate(screen: Screen): Boolean =
        sendCommand(command = Command.Navigate(screen = screen))
}
