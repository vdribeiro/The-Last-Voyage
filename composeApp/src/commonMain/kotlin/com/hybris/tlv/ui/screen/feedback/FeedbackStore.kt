package com.hybris.tlv.ui.screen.feedback

import kotlinx.coroutines.Job
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store

internal class FeedbackStore(
    private val tag: String?,
    private val message: String?,
): Store<FeedbackState, FeedbackAction>(
    initialState = FeedbackState(isError = tag != null || message != null)
) {
    private fun sendFeedback(action: FeedbackAction.SendFeedback): Job = launch(id = "sendFeedback") {
        updateState {
            it.copy(
                feedback = action.message,
                showThanks = true
            )
        }
        val feedback = buildList {
            tag?.let { add(element = "Identifier: $it") }
            message?.let { add(element = "Message: $it") }
            if (action.message.isNotBlank()) add(element = "Feedback: ${action.message}")
        }.joinToString(separator = "\n")
        Telemetry.info(tag = TAG, message = "Send feedback")
        Telemetry.feedback(message = feedback)
    }

    override fun back(state: FeedbackState) {
        when {
            state.isError -> navigate(screen = Screen.Splash)
            else -> super.back(state = state)
        }
    }

    override fun reducer(state: FeedbackState, action: FeedbackAction) {
        when (action) {
            is FeedbackAction.SendFeedback -> sendFeedback(action = action)
        }
    }

    companion object {
        private const val TAG = "Feedback"
    }
}
