package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.AudioPlayer.Action
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App() = AppTheme {
    // Setup Navigation
    val navigation = dependency.navigation
    BackHandler(enabled = true) { navigation.back() }
    val navigationState by navigation.stateFlow.collectAsState()
    val screen = navigationState.screen

    // Setup Audio Player
    val audioPlayer = dependency.audioPlayer
    LaunchedEffect(key1 = screen) {
        val playlist = getTracks(screen = screen)
        if (playlist != null) audioPlayer.action(action = Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = Action.Pause) },
        onForeground = { audioPlayer.action(action = Action.Resume) },
    )

    // Render Screen
    navigation.Screen(navigationState = navigationState)
}
