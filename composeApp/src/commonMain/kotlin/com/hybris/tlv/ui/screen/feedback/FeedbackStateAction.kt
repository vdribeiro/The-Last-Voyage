package com.hybris.tlv.ui.screen.feedback

internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}

internal sealed interface FeedbackStateBuilder {
    data object Default: FeedbackStateBuilder
    data class Error(val tag: String, val message: String): FeedbackStateBuilder
}

internal data class FeedbackState(
    val isError: Boolean = false,
    val feedback: String = "",
    val showThanks: Boolean = false
)
