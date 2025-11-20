package com.hybris.tlv.ui.screen.feedback

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.SplashScreen
import com.hybris.tlv.ui.store.Store

internal class FeedbackStore(
    private val tag: String?,
    private val message: String?
): Store<FeedbackState, FeedbackAction>(
    initialState = FeedbackState(isError = tag != null || message != null)
) {
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
