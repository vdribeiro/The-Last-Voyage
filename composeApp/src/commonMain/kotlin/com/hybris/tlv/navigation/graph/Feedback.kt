package com.hybris.tlv.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackStore

internal fun NavGraphBuilder.feedbackScreen() =
    composable<Screen.Feedback> {
        val screen = it.toRoute<Screen.Feedback>()
        FeedbackScreen(store = viewModel {
            FeedbackStore(
                tag = screen.tag,
                message = screen.message,
            )
        })
    }
