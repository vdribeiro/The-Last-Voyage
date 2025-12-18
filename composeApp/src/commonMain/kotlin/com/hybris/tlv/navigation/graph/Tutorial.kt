package com.hybris.tlv.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.screen.tutorial.TutorialScreen
import com.hybris.tlv.screen.tutorial.TutorialStore

internal fun NavGraphBuilder.tutorialScreen(
    config: ConfigManager
) = composable<Screen.Tutorial> {
    val screen = it.toRoute<Screen.Tutorial>()
    TutorialScreen(store = viewModel {
        TutorialStore(
            config = config,
            newGame = screen.newGame
        )
    })
}

