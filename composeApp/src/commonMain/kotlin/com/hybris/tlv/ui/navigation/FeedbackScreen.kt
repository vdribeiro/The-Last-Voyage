package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore

internal fun NavGraphBuilder.feedbackScreen(navController: NavHostController) =
    graph<FeedbackScreen, FeedbackStore>(
        navController = navController,
        store = { FeedbackStore(stateBuilder = FeedbackStateBuilder.Feedback) },
        screen = { FeedbackScreen(store = it) }
    )

@Serializable
internal data class FeedbackScreen(val stateBuilder: FeedbackStateBuilder): Screen
