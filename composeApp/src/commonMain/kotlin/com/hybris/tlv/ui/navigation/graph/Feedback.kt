package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.ui.navigation.Screen

internal fun NavGraphBuilder.feedbackScreen() =
    composable<Screen.Feedback> {
        val screen = it.toRoute<Screen.Feedback>()
        _root_ide_package_.com.hybris.tlv.ui.screen.feedback.FeedbackScreen(store = viewModel {
            _root_ide_package_.com.hybris.tlv.ui.screen.feedback.FeedbackStore(
                tag = screen.tag,
                message = screen.message,
            )
        })
    }
