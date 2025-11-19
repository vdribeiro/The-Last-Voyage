package com.hybris.tlv.ui.store

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder

/**
 * The central hub for a screen's [State]. It's the single source of truth for the UI.
 * It receives [Action]s from the UI and calls the appropriate Use Cases to handle the business logic for that action.
 * After it receives the result from the Use Case, it combines it with the current [State], and emits a new [State].
 * A key rule is that the UI only observes the Store's [State] and never modifies it directly.
 */
internal open class Store<State, Action>(
    private val audioPlayer: AudioPlayer,
    initialState: State
): ViewModel() {

    /**
     * The current state of the screen.
     */
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(value = initialState)
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    /**
     * Effect for navigation.
     */
    private val _effect: Channel<Screen> = Channel()
    val effect: Flow<Screen> = _effect.receiveAsFlow()

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
        viewModelScope.launch { _stateFlow.update { body(_stateFlow.value) } }

    /**
     * Navigate to a new [screen].
     */
    protected fun navigate(screen: Screen) =
        viewModelScope.launch { _effect.send(element = screen) }

    /**
     * Launches a coroutine.
     */
    protected fun launch(context: CoroutineContext = Dispatcher.IO, block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(context = context) { block() }

    /**
     * Overridable back navigation.
     */
    protected open fun goBack(state: State) {
    }

    /**
     * Navigate back.
     */
    fun back() {}

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
    fun feedback() =
        navigate(screen = Screen.Feedback(stateBuilder = FeedbackStateBuilder.Default),)

    /**
     * Navigate to [Screen.Feedback] screen with error.
     */
    fun error(tag: String, message: String) =
        navigate(screen = Screen.Feedback(stateBuilder = FeedbackStateBuilder.Error(tag = tag, message = message)),)
}
