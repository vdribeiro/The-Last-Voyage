package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen

internal fun NavGraphBuilder.feedbackScreen(storeFactory: StoreFactory) =
    composable<Screen.Feedback> {
        val screen = it.toRoute<Screen.Feedback>()
        FeedbackScreen(store = viewModel { storeFactory.getFeedbackStore(tag = screen.tag, message = screen.message) })
    }
