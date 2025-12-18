package com.hybris.tlv.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.navigation.graph.achievementScreen
import com.hybris.tlv.navigation.graph.cheatScreen
import com.hybris.tlv.navigation.graph.creditScreen
import com.hybris.tlv.navigation.graph.eventScreen
import com.hybris.tlv.navigation.graph.feedbackScreen
import com.hybris.tlv.navigation.graph.gameOverScreen
import com.hybris.tlv.navigation.graph.gameScreen
import com.hybris.tlv.navigation.graph.helpScreen
import com.hybris.tlv.navigation.graph.mainMenuScreen
import com.hybris.tlv.navigation.graph.newGameScreen
import com.hybris.tlv.navigation.graph.scoreScreen
import com.hybris.tlv.navigation.graph.splashScreen
import com.hybris.tlv.navigation.graph.stellarExplorerScreen
import com.hybris.tlv.navigation.graph.tutorialScreen
import com.hybris.tlv.usecase.UseCases

/**
 * The main navigation host for the application.
 * This composable sets up the [NavHost] and defines all the possible navigation destinations within the app,
 * linking each [Screen] to its corresponding composable content.
 */
@Composable
internal fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Splash,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        splashScreen(config = config, useCases = useCases)
        cheatScreen(config = config)
        mainMenuScreen(config = config, useCases = useCases)
        helpScreen(config = config, useCases = useCases)
        feedbackScreen()
        newGameScreen(useCases = useCases)
        tutorialScreen(config = config)
        gameScreen(config = config, useCases = useCases)
        eventScreen(useCases = useCases)
        gameOverScreen(useCases = useCases)
        stellarExplorerScreen(useCases = useCases)
        scoreScreen(useCases = useCases)
        achievementScreen(useCases = useCases)
        creditScreen(useCases = useCases)
    }
}
