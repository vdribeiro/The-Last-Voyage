package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalInspectionMode
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.media.rememberAudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App(navigation: NavigationManager) {
    AppTheme {
        BackHandler(enabled = true) { navigation.back() }

        val navigationState by navigation.stateFlow.collectAsState()
        navigation.Screen(
            screen = navigationState.screen,
            state = navigationState.state
        )

        if (!LocalInspectionMode.current) {
            val mediaPlayer = rememberAudioPlayer()
            mediaPlayer.play(playlist = getTracks(screen = navigationState.screen))
        }
    }
}

internal val core: Core by lazy {
    Core()
}
