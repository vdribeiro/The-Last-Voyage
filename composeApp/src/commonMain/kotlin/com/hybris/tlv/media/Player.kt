package com.hybris.tlv.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.ui.navigation.Screen

@Composable
internal fun AudioPlayer(
    audioPlayer: AudioPlayer,
    screen: Screen?
) {
    LaunchedEffect(key1 = screen) {
        val playlist = getTracks(screen = screen)
        if (playlist != null) audioPlayer.action(action = AudioPlayer.Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = AudioPlayer.Action.Pause) },
        onForeground = { audioPlayer.action(action = AudioPlayer.Action.Resume) },
    )
}

private fun getTracks(screen: Screen?): List<String>? = when (screen) {
    Screen.Splash,
    Screen.MainMenu,
    Screen.NewGame,
    Screen.StellarExplorer,
    Screen.Score,
    Screen.Achievement,
    Screen.Credit -> listOf(
        "tracks/ville_seppanen-1_g.mp3",
    )

    is Screen.Game,
    is Screen.Event -> listOf(
        "tracks/blind_shift.mp3",
        "tracks/graduality.mp3",
        "tracks/led_twilight.mp3",
        "tracks/my_very_own_dead_ship.mp3",
        "tracks/neon_sky.mp3",
        "tracks/rain_in_space.mp3",
        "tracks/space_gras.mp3",
    )

    Screen.GameOver -> listOf(
        "tracks/space.mp3",
    )

    Screen.Cheat,
    Screen.Help,
    is Screen.Feedback,
    is Screen.Tutorial,
    null -> null
}
