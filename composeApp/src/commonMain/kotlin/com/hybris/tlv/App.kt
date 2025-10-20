package com.hybris.tlv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.AudioPlayer.Action
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.navigation.backNavigation
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun App() = AppTheme {
    // Setup Navigation
    val navigation = dependency.navigation
    val navigationState by navigation.stateFlow.collectAsState()
    val screen = navigationState.screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .enableCheats()
            .backNavigation { navigation.back() }) {
        // Render Screen
        navigation.Screen(navigationState = navigationState)
    }

    // Setup Audio Player
    val audioPlayer = dependency.audioPlayer
    LifecycleCoroutine(screen) {
        val playlist = getTracks(screen = screen)
        if (playlist != null) audioPlayer.action(action = Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = Action.Pause) },
        onForeground = { audioPlayer.action(action = Action.Resume) },
    )
}
