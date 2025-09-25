package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.ui.navigation.NavigationManager

internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}

internal data class FeedbackStateBuilder(
    val navigationState: NavigationManager.State = NavigationManager.State(),
    val tag: String? = null,
    val message: String? = null,
)

internal data class FeedbackState(
    val isError: Boolean,
)
