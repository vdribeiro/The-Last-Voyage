package com.hybris.tlv.media

import com.hybris.tlv.ui.navigation.NavigationManager.Screen

internal fun <StateBuilder> getTracks(screen: Screen<StateBuilder>): Array<String>? = when (screen) {
    Screen.Feedback -> null

    Screen.Splash,
    Screen.MainMenu,
    Screen.NewGame,
    Screen.Tutorial,
    Screen.StellarExplorer,
    Screen.Score,
    Screen.Achievement,
    Screen.Credit -> arrayOf(
        "tracks/ville_seppanen-1_g.mp3",
    )

    Screen.Game,
    Screen.Event -> arrayOf(
        "tracks/blind_shift.mp3",
        "tracks/graduality.mp3",
        "tracks/led_twilight.mp3",
        "tracks/my_very_own_dead_ship.mp3",
        "tracks/neon_sky.mp3",
        "tracks/rain_in_space.mp3",
        "tracks/space_gras.mp3",
    )

    Screen.GameOver -> arrayOf(
        "tracks/space.mp3",
    )
}
