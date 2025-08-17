package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.media.rememberAudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun App(navigation: NavigationManager) {
    AppTheme {
        val mediaPlayer = rememberAudioPlayer()
        val navigationState by navigation.stateFlow.collectAsState()

        if (!navigationState.music) mediaPlayer.stop() else {
            mediaPlayer.play(*getTracks(screen = navigationState.screen))
        }

        navigation.Screen(
            screen = navigationState.screen,
            state = navigationState.state
        )
    }
}
