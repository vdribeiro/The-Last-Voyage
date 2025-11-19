package com.hybris.tlv.media

import com.hybris.tlv.ui.navigation.Screen

internal fun getTracks(screen: Screen): List<String>? = when (screen) {
    Screen.Help,
    Screen.Feedback,
    Screen.Tutorial -> null

    Screen.Splash,
    Screen.MainMenu,
    Screen.NewGame,
    Screen.StellarExplorer,
    Screen.Score,
    Screen.Achievement,
    Screen.Credit -> listOf(
        "tracks/ville_seppanen-1_g.mp3",
    )

    Screen.Game,
    Screen.Event -> listOf(
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

    is Screen.Feedback -> null
    is Screen.Event -> null
    is Screen.Game -> null
    is Screen.Tutorial -> null
}
