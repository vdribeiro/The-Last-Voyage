package com.hybris.tlv.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.navigation.graph.achievementScreen
import com.hybris.tlv.ui.navigation.graph.catastropheExplorerScreen
import com.hybris.tlv.ui.navigation.graph.catastropheScreen
import com.hybris.tlv.ui.navigation.graph.cheatScreen
import com.hybris.tlv.ui.navigation.graph.creditScreen
import com.hybris.tlv.ui.navigation.graph.eventExplorerScreen
import com.hybris.tlv.ui.navigation.graph.eventScreen
import com.hybris.tlv.ui.navigation.graph.feedbackScreen
import com.hybris.tlv.ui.navigation.graph.gameOverScreen
import com.hybris.tlv.ui.navigation.graph.gameScreen
import com.hybris.tlv.ui.navigation.graph.helpScreen
import com.hybris.tlv.ui.navigation.graph.mainMenuScreen
import com.hybris.tlv.ui.navigation.graph.newGameScreen
import com.hybris.tlv.ui.navigation.graph.scoreScreen
import com.hybris.tlv.ui.navigation.graph.splashScreen
import com.hybris.tlv.ui.navigation.graph.stellarExplorerScreen
import com.hybris.tlv.ui.navigation.graph.tutorialScreen

internal val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }

/**
 * The main navigation host for the application.
 * This composable sets up the [NavHost] and defines all the possible navigation destinations within the app,
 * linking each [Screen] to its corresponding composable content.
 */
@Composable
internal fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = LocalNavController.current ?: rememberNavController(),
    config: ConfigManager,
    useCases: UseCases
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Splash(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        splashScreen(config = config, useCases = useCases)
        cheatScreen(config = config)
        mainMenuScreen(config = config, useCases = useCases)
        helpScreen(config = config)
        feedbackScreen()
        newGameScreen(useCases = useCases)
        catastropheScreen(useCases = useCases)
        tutorialScreen(config = config)
        gameScreen(config = config, useCases = useCases)
        eventScreen(useCases = useCases)
        gameOverScreen(useCases = useCases)
        stellarExplorerScreen(useCases = useCases)
        scoreScreen(useCases = useCases)
        achievementScreen(useCases = useCases)
        creditScreen(useCases = useCases)
        catastropheExplorerScreen(useCases = useCases)
        eventExplorerScreen(useCases = useCases)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun Navigation(
    navController: NavHostController,
    onBack: (() -> Unit)?
) {
    // TODO - Replace deprecated BackHandler
    BackHandler { onBack?.invoke() }
}
