package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.ui.navigation.NavigationManager.NavigationState

internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}

internal data class FeedbackStateBuilder(
    val navigationState: NavigationState = NavigationState(),
    val tag: String? = null,
    val message: String? = null,
)

internal data class FeedbackState(
    val isError: Boolean,
)
