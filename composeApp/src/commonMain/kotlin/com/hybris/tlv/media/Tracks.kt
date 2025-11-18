package com.hybris.tlv.media

import com.hybris.tlv.ui.navigation.Route

internal fun getTracks(route: Route): List<String>? = when (route) {
    Route.Help,
    Route.Feedback,
    Route.Tutorial -> null

    Route.Splash,
    Route.MainMenu,
    Route.NewGame,
    Route.StellarExplorer,
    Route.Score,
    Route.Achievement,
    Route.Credit -> listOf(
        "tracks/ville_seppanen-1_g.mp3",
    )

    Route.Game,
    Route.Event -> listOf(
        "tracks/blind_shift.mp3",
        "tracks/graduality.mp3",
        "tracks/led_twilight.mp3",
        "tracks/my_very_own_dead_ship.mp3",
        "tracks/neon_sky.mp3",
        "tracks/rain_in_space.mp3",
        "tracks/space_gras.mp3",
    )

    Route.GameOver -> listOf(
        "tracks/space.mp3",
    )
}
