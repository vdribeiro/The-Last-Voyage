package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.tutorial.TutorialScreen
import com.hybris.tlv.ui.screen.tutorial.TutorialStore

internal fun NavGraphBuilder.tutorialScreen(navController: NavHostController) =
    graph<TutorialScreen, TutorialStore>(
        navController = navController,
        store = { TutorialStore(newGame = it.newGame) },
        screen = { TutorialScreen(store = it) }
    )

@Serializable
internal data class TutorialScreen(val newGame: Boolean = false): Screen
