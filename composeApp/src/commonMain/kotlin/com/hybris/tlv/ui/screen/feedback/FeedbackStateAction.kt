package com.hybris.tlv.ui.screen.feedback

internal data class FeedbackState(
    val tag: String? = null,
    val message: String? = null,
)

internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}
