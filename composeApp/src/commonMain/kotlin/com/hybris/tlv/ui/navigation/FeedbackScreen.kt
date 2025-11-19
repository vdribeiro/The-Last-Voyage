package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore

internal fun NavGraphBuilder.feedbackScreen() =
    graph<FeedbackScreen, FeedbackStore>(
        store = { FeedbackStore(stateBuilder = it.stateBuilder) },
        screen = { FeedbackScreen(store = it) }
    )

@Serializable
internal data class FeedbackScreen(val stateBuilder: FeedbackStateBuilder): Screen
