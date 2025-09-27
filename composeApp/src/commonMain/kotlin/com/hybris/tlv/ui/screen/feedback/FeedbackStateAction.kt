package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.ui.navigation.NavigationManager.NavigationState

internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}

internal sealed interface FeedbackStateBuilder {
    data class Feedback(val navigationState: NavigationState): FeedbackStateBuilder
    data class Error(val tag: String, val message: String): FeedbackStateBuilder
}

internal data class FeedbackState(
    val isError: Boolean = false
)
