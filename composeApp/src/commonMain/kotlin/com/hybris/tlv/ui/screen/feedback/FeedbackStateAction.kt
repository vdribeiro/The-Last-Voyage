package com.hybris.tlv.ui.screen.feedback


internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}

internal data class FeedbackStateBuilder(
    val tag: String? = null,
    val message: String? = null,
)

internal data class FeedbackState(
    val isError: Boolean,
)
