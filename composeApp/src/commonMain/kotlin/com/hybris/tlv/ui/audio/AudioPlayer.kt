package com.hybris.tlv.ui.audio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.ui.lifecycle.Register

internal val LocalAudioPlayer = staticCompositionLocalOf { AudioPlayer() }

/**
 * Composable that manages audio playback based on the current navigation destination and app lifecycle.
 */
@Composable
internal fun AudioPlayer(
    navController: NavHostController,
    audioPlayer: AudioPlayer
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination
    LaunchedEffect(key1 = destination) {
        getTracks(destination = destination)?.let { playlist ->
            audioPlayer.action(action = AudioPlayer.Action.Play(playlist = playlist))
        }
    }

    Register(
        onBackground = { audioPlayer.action(action = AudioPlayer.Action.Pause) },
        onForeground = { audioPlayer.action(action = AudioPlayer.Action.Resume) },
    )
}
