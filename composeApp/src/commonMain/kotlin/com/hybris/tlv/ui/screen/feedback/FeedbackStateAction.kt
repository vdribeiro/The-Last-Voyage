package com.hybris.tlv.ui.screen.feedback

import kotlinx.serialization.Serializable

internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}

@Serializable
internal sealed interface FeedbackStateBuilder {
    @Serializable
    data object Feedback: FeedbackStateBuilder
    @Serializable
    data class Error(val tag: String, val message: String): FeedbackStateBuilder
}

internal data class FeedbackState(
    val isError: Boolean = false,
    val feedback: String = "",
    val showThanks: Boolean = false
)
