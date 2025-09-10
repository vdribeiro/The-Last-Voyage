package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.ui.navigation.NavigationManager.Screen

internal data class FeedbackState(
    val screen: Screen? = null,
    val throwable: Throwable? = null,
    val identifier: String? = null,
)

internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}
