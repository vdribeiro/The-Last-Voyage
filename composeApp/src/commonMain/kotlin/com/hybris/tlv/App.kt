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
    // Setup Navigation
    val navigation = core.navigation
    BackHandler(enabled = true) { navigation.back() }
    val navigationState by navigation.stateFlow.collectAsState()
    navigation.Screen(state = navigationState)

    // Setup Audio Player
    val audioPlayer = core.audioPlayer
    val screen = navigationState.screen
    LaunchedEffect(keys = arrayOf(screen)) {
        val playlist = getTracks(screen = screen)
        if (playlist != null) audioPlayer.play(playlist = playlist)
    }
    Register(
        onBackground = { audioPlayer.pause() },
        onForeground = { audioPlayer.resume() },
    )
}

internal val core: Core by lazy {
    Core()
}
