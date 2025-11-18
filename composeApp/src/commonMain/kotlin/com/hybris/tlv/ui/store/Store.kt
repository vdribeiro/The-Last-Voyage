package com.hybris.tlv.ui.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder

/**
 * The central hub for a screen's [State]. It's the single source of truth for the UI.
 * It receives [Action]s from the UI and calls the appropriate Use Cases to handle the business logic for that action.
 * After it receives the result from the Use Case, it combines it with the current [State], and emits a new [State].
 * A key rule is that the UI only observes the Store's [State] and never modifies it directly.
 */
internal open class Store<State, Action>(
    private val navigation: NavigationManager,
    private val audioPlayer: AudioPlayer,
    initialState: State
) {
    /**
     * The current state of the screen.
     */
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()
    /**
     * The list of jobs launched by the Store.
     */
    private val jobs = mutableListOf<Job>()

    init {
        navigation.back = { goBack(state = _stateFlow.value) }
    }

    /**
     * Get the savable state of the store.
     */
    protected open fun getSavableState(state: State): Any? = null

    /**
     * Overridable back navigation.
     */
    protected open fun goBack(state: State) {
        navigation.goBack()
    }

    /**
     * Clean up the store and navigate to a new [screen] given an optional [stateBuilder].
     */
    protected fun navigate(screen: Screen, stateBuilder: Any? = null) {
        jobs.forEach { it.cancel() }
        saveState()
        navigation.navigate(navigationState = NavigationState(screen = screen, stateBuilder = stateBuilder))
    }

    /**
     * Save the current state of the store to the navigation stack.
     */
    private fun saveState(): Job = navigation.saveState(stateBuilder = getSavableState(state = _stateFlow.value))

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
        Dispatcher.Main.launch {
            _stateFlow.update { body(_stateFlow.value) }
            saveState()
        }

    /**
     * Launches a coroutine and adds it to the list of jobs.
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        Dispatcher.IO.launch { block() }.also { jobs.add(element = it) }

    /**
     * Navigate back.
     */
    fun back() = navigation.back()

    /**
     * Toggle audio player.
     */
    fun toggleAudio() = audioPlayer.action(action = AudioPlayer.Action.Toggle)

    /**
     * Navigate to [Screen.Help] screen.
     */
    fun help() = navigate(screen = Screen.Help)

    /**
     * Navigate to [Screen.Feedback] screen asking for feedback.
     */
    fun feedback() = navigate(
        screen = Screen.Feedback,
        stateBuilder = FeedbackStateBuilder.Default
    )

    /**
     * Navigate to [Screen.Feedback] screen with error.
     */
    fun error(tag: String, message: String) = navigate(
        screen = Screen.Feedback,
        stateBuilder = FeedbackStateBuilder.Error(tag = tag, message = message)
    )
}
