package com.hybris.tlv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.media.AudioPlayer.Action
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.ScreenBuilder
import com.hybris.tlv.ui.navigation.backNavigation
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun App(dependency: Dependency) = AppTheme {
    val config = dependency.config
    val audioPlayer = dependency.audioPlayer
    val navigation = dependency.navigation
    val screenBuilder = dependency.screenBuilder

    // Setup Navigation
    val navigationState by navigation.stateFlow.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .enableGestureCheats(config = config)
            .backNavigation { navigation.back() }) {
        // Render Screen
        screenBuilder.Screen(navigationState = navigationState)
    }

    // Setup Audio Player
    LifecycleCoroutine(navigationState.screen) {
        val playlist = getTracks(screen = navigationState.screen)
        if (playlist != null) audioPlayer.action(action = Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = Action.Pause) },
        onForeground = { audioPlayer.action(action = Action.Resume) },
    )
}
