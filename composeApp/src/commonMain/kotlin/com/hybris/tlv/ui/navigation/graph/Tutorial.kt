package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.tutorial.TutorialScreen
import com.hybris.tlv.ui.screen.tutorial.TutorialStore

internal fun NavGraphBuilder.tutorialScreen() =
    composable<Screen.Tutorial> {
        val screen = it.toRoute<Screen.Tutorial>()
        TutorialScreen(store = viewModel {
            TutorialStore(newGame = screen.newGame)
        })
    }

