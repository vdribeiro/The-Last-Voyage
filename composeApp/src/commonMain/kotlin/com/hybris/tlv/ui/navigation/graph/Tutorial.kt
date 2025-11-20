package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.tutorial.TutorialScreen
import com.hybris.tlv.ui.screen.tutorial.TutorialStore

internal fun NavGraphBuilder.tutorialScreen(
    navController: NavHostController
) = graph<Screen.Tutorial, TutorialStore>(
    navController = navController,
    store = { TutorialStore(newGame = it.newGame) },
    screen = { TutorialScreen(store = it) }
)
