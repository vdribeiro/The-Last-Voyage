package com.hybris.tlv.ui.audio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hybris.tlv.core.audio.AudioPlayer
import com.hybris.tlv.core.resource.AudioResource
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.ui.lifecycle.Register
import com.hybris.tlv.ui.navigation.Screen

/**
 * Composable that manages audio playback based on the current navigation destination and app lifecycle.
 */
@Composable
internal fun AudioPlayer(
    navController: NavHostController,
    audioPlayer: AudioPlayer,
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

/**
 * Determines the appropriate playlist based on the current navigation destination.
 * Null means that the playback should remain unchanged.
 */
private fun getTracks(destination: NavDestination?): List<AudioResource>? = runCatching {
    when {
        destination == null ||
                destination.hasRoute<Screen.Cheat>() ||
                destination.hasRoute<Screen.Help>() ||
                destination.hasRoute<Screen.Feedback>() ||
                destination.hasRoute<Screen.Tutorial>() -> null

        destination.hasRoute<Screen.Splash>() ||
                destination.hasRoute<Screen.MainMenu>() ||
                destination.hasRoute<Screen.NewGame>() ||
                destination.hasRoute<Screen.StellarExplorer>() ||
                destination.hasRoute<Screen.Score>() ||
                destination.hasRoute<Screen.Achievement>() ||
                destination.hasRoute<Screen.Credit>() -> listOf(
            AudioResource.VilleSeppanen
        )

        destination.hasRoute<Screen.Catastrophe>() ||
                destination.hasRoute<Screen.Game>() ||
                destination.hasRoute<Screen.Event>() -> listOf(
            AudioResource.BlindShift,
            AudioResource.Graduality,
            AudioResource.LedTwilight,
            AudioResource.NeonSky,
            AudioResource.RainInSpace,
            AudioResource.SpaceGras
        )

        destination.hasRoute<Screen.GameOver>() -> listOf(
            AudioResource.Space
        )

        else -> null
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get tracks", throwable = it) }.getOrNull()

private const val TAG = "AudioPlayer"
