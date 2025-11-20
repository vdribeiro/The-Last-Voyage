package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackStore

internal fun NavGraphBuilder.feedbackScreen(
    navController: NavHostController,
    config: ConfigManager
) = graph<Screen.Feedback, FeedbackStore>(
    navController = navController,
    store = {
        FeedbackStore(
            tag = it.tag,
            message = it.message,
            config = config
        )
    },
    screen = { FeedbackScreen(store = it) }
)
