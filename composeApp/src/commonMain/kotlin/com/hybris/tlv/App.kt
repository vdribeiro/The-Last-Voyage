package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App() = AppTheme {
    val navigation = core.navigation

    // Setup Navigation
    BackHandler(enabled = true) { navigation.back() }
    val navigationState by navigation.stateFlow.collectAsState()
    navigation.Screen(state = navigationState)

    // Setup Audio Player
    val audioPlayer = core.audioPlayer
    if (audioPlayer != null) {
        val screen = navigationState.screen
        LaunchedEffect(keys = arrayOf(screen)) {
            val playlist = getTracks(screen = screen)
            if (playlist.isNotEmpty()) audioPlayer.play(playlist = playlist)
        }
        Register(
            onPause = { audioPlayer.pause() },
            onResume = { audioPlayer.resume() },
        )
    }
}

internal val core: Core by lazy {
    Core()
}
