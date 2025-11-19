package com.hybris.tlv

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.SplashScreen
import com.hybris.tlv.ui.navigation.helpScreen
import com.hybris.tlv.ui.navigation.mainMenuScreen
import com.hybris.tlv.ui.navigation.splashScreen
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.UseCases

@Composable
internal fun App(
    config: ConfigManager,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) = AppTheme {
    // Setup Navigation
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier
            .enableGestureCheats(config = config)
            .enableKeyCheats(config = config),
        navController = navController,
        startDestination = SplashScreen,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        splashScreen(navController = navController, useCases = useCases, config = config)
        mainMenuScreen(navController = navController, useCases = useCases, config = config)
        helpScreen(navController = navController, useCases = useCases, config = config)
//        feedbackScreen()
//        newGameScreen(useCases = useCases)
//        tutorialScreen()
//        gameScreen(useCases = useCases)
//        eventScreen(useCases = useCases)
    }

    // Setup Audio Player
    val currentScreen = navController.currentDestination as? Screen
    LifecycleCoroutine(currentScreen) {
        val playlist = currentScreen?.let { getTracks(screen = it) }
        if (playlist != null) audioPlayer.action(action = AudioPlayer.Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = AudioPlayer.Action.Pause) },
        onForeground = { audioPlayer.action(action = AudioPlayer.Action.Resume) },
    )
}
