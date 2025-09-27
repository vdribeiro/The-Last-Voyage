package com.hybris.tlv.ui.store

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.NavigationState
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * The central hub for a screen's [State]. It's the single source of truth for the UI.
 * It receives [Action]s from the UI and calls the appropriate Use Cases to handle the business logic for that action.
 * After it receives the result from the Use Case, it combines it with the current [State], and emits a new [State].
 * A key rule is that the UI only observes the Store's [State] and never modifies it directly.
 */
internal open class Store<State, Action>(
    private val dispatcher: Dispatcher,
    private val navigation: NavigationManager,
    private val audioPlayer: AudioPlayer?,
    initialState: State
) {

    /**
     * The current state of the screen.
     */
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<State> get() = _stateFlow
    /**
     * The list of jobs launched by the Store.
     */
    private val jobs = mutableListOf<Job>()
    /**
     * The current navigation state.
     */
    private val navigationState: NavigationState get() = NavigationState(screen = navigation.stateFlow.value.screen, state = _stateFlow.value)

    init {
        navigation.back = { back(state = _stateFlow.value).invoke() }
    }

    /**
     * Clean up the store and navigate to a new [screen] given an optional [state].
     */
    protected fun navigate(screen: Screen, state: Any? = null) {
        navigation.back = {}
        jobs.forEach { it.cancel() }
        navigation.navigate(screen = screen, state = state)
    }

    /**
     * Back navigation.
     */
    protected open fun back(state: State): () -> Unit = {}

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
    protected fun updateState(body: (State) -> State): Job =
        dispatcher.main.launch { _stateFlow.update { body(_stateFlow.value) } }

    /**
     * Launches a coroutine and adds it to the list of jobs.
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        dispatcher.io.launch { block() }.also { jobs.add(element = it) }

    /**
     * Toggle audio player.
     */
    fun toggleAudio() = audioPlayer?.toggle()

    /**
     * Navigate to feedback screen.
     */
    fun feedback() = navigate(
        screen = Screen.FEEDBACK,
        state = FeedbackStateBuilder(navigationState = navigationState)
    )
}
