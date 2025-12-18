package com.hybris.tlv.audio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.telemetry.Telemetry

/**
 * Composable that manages audio playback based on the current navigation destination and app lifecycle.
 */
@Composable
internal fun AudioPlayer(
    audioPlayer: AudioPlayer,
    destination: NavDestination?
) {
    val playlist = getTracks(destination = destination)
    // Updates playlist based on destination; null means that the playback remains unchanged
    LaunchedEffect(key1 = playlist) {
        audioPlayer.action(action = AudioPlayer.Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = AudioPlayer.Action.Pause) },
        onForeground = { audioPlayer.action(action = AudioPlayer.Action.Resume) },
    )
}

/**
 * Determines the appropriate playlist based on the current navigation destination, or null if no tracks are associated with the current screen.
 */
private fun getTracks(destination: NavDestination?): List<String>? = runCatching {
    when {
        destination == null -> null
        destination.hasRoute<Screen.Splash>() ||
                destination.hasRoute<Screen.MainMenu>() ||
                destination.hasRoute<Screen.NewGame>() ||
                destination.hasRoute<Screen.StellarExplorer>() ||
                destination.hasRoute<Screen.Score>() ||
                destination.hasRoute<Screen.Achievement>() ||
                destination.hasRoute<Screen.Credit>() -> listOf(
            "tracks/ville_seppanen-1_g.mp3"
        )

        destination.hasRoute<Screen.Game>() ||
                destination.hasRoute<Screen.Event>() -> listOf(
            "tracks/blind_shift.mp3",
            "tracks/graduality.mp3",
            "tracks/led_twilight.mp3",
            "tracks/my_very_own_dead_ship.mp3",
            "tracks/neon_sky.mp3",
            "tracks/rain_in_space.mp3",
            "tracks/space_gras.mp3",
        )

        destination.hasRoute<Screen.GameOver>() -> listOf(
            "tracks/space.mp3"
        )

        else -> null
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get tracks", throwable = it) }.getOrNull()

private const val TAG = "AudioPlayer"
