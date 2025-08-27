package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.media.rememberAudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App(navigation: NavigationManager) {
    AppTheme {
        BackHandler(enabled = true) { navigation.back() }

        val mediaPlayer = rememberAudioPlayer()
        val navigationState by navigation.stateFlow.collectAsState()

        mediaPlayer.play(playlist = getTracks(screen = navigationState.screen))

        navigation.Screen(
            screen = navigationState.screen,
            state = navigationState.state
        )
    }
}
