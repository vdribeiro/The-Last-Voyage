package com.hybris.tlv.ui.screen.feedback

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.hybris.tlv.core.telemetry.Console
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store

internal class FeedbackStore(
    private val tag: String?,
    private val message: String?,
): Store<FeedbackState, FeedbackAction>(
    initialState = FeedbackState(isError = tag != null || message != null)
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        while (isActive) {
            val logs = Console.getSnapshot().joinToString(separator = "\n").ifBlank { null }
            updateState { it.copy(logs = logs) }
            delay(timeMillis = 500)
        }
    }

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

    private fun navigateBack(state: FeedbackState) {
        when {
            state.isError -> navigate(screen = Screen.Splash(reset = true))
            else -> navigateBack()
        }
    }

    override fun reducer(state: FeedbackState, action: FeedbackAction) {
        when (action) {
            FeedbackAction.Back -> navigateBack(state = state)
            is FeedbackAction.SendFeedback -> sendFeedback(action = action)
        }
    }

    companion object {
        private const val TAG = "Feedback"
    }
}
