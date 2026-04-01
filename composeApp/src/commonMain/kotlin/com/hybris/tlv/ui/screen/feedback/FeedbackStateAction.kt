package com.hybris.tlv.ui.screen.feedback

internal sealed interface FeedbackAction {
    data object Back: FeedbackAction
    data class SendFeedback(val message: String): FeedbackAction
}

internal data class FeedbackState(
    val isError: Boolean = false,
    val feedback: String = "",
    val showThanks: Boolean = false,
    val logs: String? = null
)
