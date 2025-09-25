package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.store.Store
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

internal class FeedbackStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    private val stateBuilder: FeedbackStateBuilder,
): Store<FeedbackAction, FeedbackState>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = FeedbackState(
        isError = stateBuilder.message != null
    )
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        val tag = stateBuilder.tag
        val message = stateBuilder.message
        if (tag != null && message != null) Logger.error(tag = tag, message = message)
    }

    private fun sendFeedback(state: FeedbackState, message: String): Job = launch {
        // Construct the feedback message with all the components
        val feedback = buildList {
            stateBuilder.tag?.let { add(element = "Identifier: $it") }
            stateBuilder.message?.let { add(element = "Message: $it") }
            if (message.isNotBlank()) add(element = "Feedback: $message")
        }.joinToString(separator = "\n")
        // TODO: Send feedback to server
        println(feedback)
        delay(timeMillis = 2000L)
        back(state = state).invoke()
    }

    override fun back(state: FeedbackState): () -> Unit = {
        val state = stateBuilder.navigationState
        navigate(screen = state.screen, stateBuilder = state.stateBuilder)
    }

    override fun reducer(state: FeedbackState, action: FeedbackAction) {
        when (action) {
            is FeedbackAction.SendFeedback -> sendFeedback(state = state, message = action.message)
        }
    }
}
