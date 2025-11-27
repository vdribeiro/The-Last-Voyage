package com.hybris.tlv.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.ui.navigation.Screen

@Composable
internal fun AudioPlayer(
    audioPlayer: AudioPlayer,
    navBackStackEntry: NavBackStackEntry?
) {
    LaunchedEffect(key1 = navBackStackEntry) {
        val playlist = getTracks(navBackStackEntry = navBackStackEntry)
        if (playlist != null) audioPlayer.action(action = AudioPlayer.Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = AudioPlayer.Action.Pause) },
        onForeground = { audioPlayer.action(action = AudioPlayer.Action.Resume) },
    )
}

private fun getTracks(navBackStackEntry: NavBackStackEntry?): List<String>? {
    val destination = navBackStackEntry?.destination ?: return null

    return when {
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
}
