package com.hybris.tlv

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.Action
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph.achievementScreen
import com.hybris.tlv.ui.navigation.graph.creditScreen
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
import com.hybris.tlv.ui.navigation.navigate
import com.hybris.tlv.ui.navigation.toScreen
import com.hybris.tlv.ui.store.action
import com.hybris.tlv.ui.store.navigation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.UseCases

@Composable
internal fun App(
    modifier: Modifier = Modifier,
    config: ConfigManager,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) = AppTheme {
    val navController = rememberNavController()

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
        mainMenuScreen(config = config, useCases = useCases)
        helpScreen(config = config, useCases = useCases)
        feedbackScreen()
        newGameScreen(useCases = useCases)
        tutorialScreen()
        gameScreen(useCases = useCases)
        eventScreen(useCases = useCases)
        gameOverScreen(useCases = useCases)
        stellarExplorerScreen(useCases = useCases)
        scoreScreen(useCases = useCases)
        achievementScreen(useCases = useCases)
        creditScreen(useCases = useCases)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(key1 = navBackStackEntry) {
        navigation.collect { screen -> navController.navigate(screen = screen) }
    }
    LaunchedEffect(key1 = navBackStackEntry) {
        action.collect { action ->
            when (action) {
                Action.Back -> navController.popBackStack()
                Action.ToggleAudio -> audioPlayer.action(action = AudioPlayer.Action.Toggle)
            }
        }
    }

    val screen = remember(key1 = navBackStackEntry) { navBackStackEntry?.toScreen() }
    AudioPlayer(
        audioPlayer = audioPlayer,
        screen = screen
    )
}
