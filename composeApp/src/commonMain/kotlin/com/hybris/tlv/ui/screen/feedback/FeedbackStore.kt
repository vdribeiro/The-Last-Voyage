package com.hybris.tlv.ui.screen.feedback

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.store.Store
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

internal class FeedbackStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: FeedbackStateBuilder,
): Store<FeedbackState, FeedbackAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        is FeedbackStateBuilder.Feedback -> FeedbackState(isError = false)
        is FeedbackStateBuilder.Error -> FeedbackState(isError = true)
    }
) {
    @get:VisibleForTesting
    internal var tag: String? = null
    @get:VisibleForTesting
    internal var message: String? = null

    init {
        when (stateBuilder) {
            is FeedbackStateBuilder.Feedback -> {}
            is FeedbackStateBuilder.Error -> {
                tag = stateBuilder.tag
                message = stateBuilder.message
                Telemetry.error(tag = tag.orEmpty(), message = message.orEmpty())
            }
        }
    }

    private fun sendFeedback(state: FeedbackState, action: FeedbackAction.SendFeedback): Job = launch {
        // Construct the feedback message with all the components
        val feedback = buildList {
            tag?.let { add(element = "Identifier: $it") }
            message?.let { add(element = "Message: $it") }
            if (action.message.isNotBlank()) add(element = "Feedback: ${action.message}")
        }.joinToString(separator = "\n")
        Telemetry.feedback(message = feedback)
        delay(timeMillis = 2000L)

        when {
            state.isError -> navigate(screen = NavigationManager.Screen.Splash)
            else -> goBack(state = state)
        }
    }

    override fun reducer(state: FeedbackState, action: FeedbackAction) {
        when (action) {
            is FeedbackAction.SendFeedback -> sendFeedback(state = state, action = action)
        }
    }
}
