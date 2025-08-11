package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.media.rememberAudioPlayer
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun App(core: Core) {
    AppTheme {
        val mediaPlayer = rememberAudioPlayer()
        val navigation = remember { Navigation(core = core) }
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
