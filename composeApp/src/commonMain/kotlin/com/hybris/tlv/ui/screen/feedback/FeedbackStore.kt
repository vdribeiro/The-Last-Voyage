package com.hybris.tlv.ui.screen.feedback

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.SplashScreen
import com.hybris.tlv.ui.store.Store

internal class FeedbackStore(
    stateBuilder: FeedbackStateBuilder,
): Store<FeedbackState, FeedbackAction>(
    initialState = when (stateBuilder) {
        FeedbackStateBuilder.Default -> FeedbackState()
        is FeedbackStateBuilder.Error -> FeedbackState(isError = true)
    }
) {
    @get:VisibleForTesting
    internal var tag: String? = null
    @get:VisibleForTesting
    internal var message: String? = null

    init {
        when (stateBuilder) {
            FeedbackStateBuilder.Default -> {}
            is FeedbackStateBuilder.Error -> {
                tag = stateBuilder.tag
                message = stateBuilder.message
            }
        }
    }

    private fun sendFeedback(state: FeedbackState, action: FeedbackAction.SendFeedback): Job = launch {
        updateState {
            it.copy(
                feedback = action.message,
                showThanks = true
            )
        }
        Telemetry.info(tag = TAG, message = "Construct the feedback message with all the components")
        val feedback = buildList {
            tag?.let { add(element = "Identifier: $it") }
            message?.let { add(element = "Message: $it") }
            if (action.message.isNotBlank()) add(element = "Feedback: ${action.message}")
        }.joinToString(separator = "\n")
        Telemetry.info(tag = TAG, message = "Send feedback")
        Telemetry.feedback(message = feedback)
        Telemetry.info(tag = TAG, message = "Wait a small time to properly thank the user")
        delay(timeMillis = 2000L)
        Telemetry.info(tag = TAG, message = "Navigate away")
        when {
            state.isError -> navigate(screen = SplashScreen)
            else -> back(state = state)
        }
    }

    override fun reducer(state: FeedbackState, action: FeedbackAction) {
        when (action) {
            is FeedbackAction.SendFeedback -> sendFeedback(state = state, action = action)
        }
    }

    companion object {
        private const val TAG = "Feedback"
    }
}
