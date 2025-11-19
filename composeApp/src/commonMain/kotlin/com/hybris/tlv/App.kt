package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.navigation.MainMenuScreen
import com.hybris.tlv.ui.navigation.SplashScreen
import com.hybris.tlv.ui.navigation.eventGraph
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.UseCases

@Composable
internal fun App(
    modifier: Modifier = Modifier,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) = AppTheme {
    // Setup Navigation
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashScreen
    ) {
        eventGraph(navController = navController, useCases = useCases)
    }

    // Setup Audio Player
    // TODO - current screen - navController.currentDestination?
    val currentScreen = MainMenuScreen
    LifecycleCoroutine(currentScreen) {
        val playlist = getTracks(screen = currentScreen)
        if (playlist != null) audioPlayer.action(action = AudioPlayer.Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = AudioPlayer.Action.Pause) },
        onForeground = { audioPlayer.action(action = AudioPlayer.Action.Resume) },
    )
}
