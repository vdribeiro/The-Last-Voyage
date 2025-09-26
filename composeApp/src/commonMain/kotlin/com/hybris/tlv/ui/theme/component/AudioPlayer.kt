package com.hybris.tlv.ui.theme.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hybris.tlv.core
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.navigation.NavigationManager

@Composable
internal fun AudioPlayer(screen: NavigationManager.Screen) {
    val audioPlayer = core.audioPlayer
    if (audioPlayer != null) {
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
