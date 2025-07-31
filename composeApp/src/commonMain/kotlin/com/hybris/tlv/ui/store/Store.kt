package com.hybris.tlv.ui.store

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.ui.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * The central hub for a screen's [State]. It's the single source of truth for the UI.
 * It receives [Action]s from the UI and calls the appropriate Use Cases to handle the business logic for that action.
 * After it receives the result from the Use Case, it combines it with the current [State], and emits a new [State].
 * A key rule is that the UI only observes the Store's [State] and never modifies it directly.
 */
internal abstract class Store<Action, State>(
    private val dispatcher: Dispatcher,
    private val navigation: Navigation,
    initialState: State
) {
    /**
     * The current state of the screen.
     */
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<State> get() = _stateFlow
    /**
     * Used for actions that must be pipelined.
     */
    private val pipeline = Mutex()
    /**
     * The list of jobs launched by the Store.
     */
    private val jobs = mutableListOf<Job>()

    /**
     * Sends an [Action] to the Store.
     */
    fun send(action: Action) = reducer(state = _stateFlow.value, action = action)

    /**
     * Called when an [Action] is sent to the Store.
     * It uses the current [state] and the [action] to produce a new [State].
     */
    protected abstract fun reducer(state: State, action: Action)

    /**
     * Updates the current [State].
     */
    protected fun updateState(body: (State) -> State) =
        dispatcher.main.launch { _stateFlow.update { body(_stateFlow.value) } }

    /**
     * Launches a coroutine and adds it to the list of jobs.
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        dispatcher.io.launch { block() }.also { jobs.add(element = it) }

    /**
     * Launches a coroutine, adds it to the list of jobs and pipelines it.
     */
    protected fun launchInPipeline(block: suspend CoroutineScope.() -> Unit): Job =
        dispatcher.io.launch { pipeline.withLock { block() } }.also { jobs.add(element = it) }

    /**
     * Launches a coroutine without adding it to the list of jobs.
     * This is useful for coroutines that you don't want to cancel when the Store is destroyed.
     * NOTE: Use this with caution as it can lead to memory leaks.
     */
    protected fun launchAndForget(block: suspend CoroutineScope.() -> Unit): Job =
        dispatcher.io.launch { block() }

    /**
     * Navigates to a new [screen] given an optional [state].
     */
    protected fun navigate(screen: Navigation.Screen, state: Any? = null) {
        jobs.forEach { it.cancel() }
        navigation.navigate(screen = screen, state = state)
    }
}
