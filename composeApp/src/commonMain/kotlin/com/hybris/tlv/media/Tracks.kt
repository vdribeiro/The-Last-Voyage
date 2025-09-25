package com.hybris.tlv.media

import com.hybris.tlv.ui.navigation.NavigationManager.Screen

internal fun getTracks(screen: Screen) = when (screen) {
    Screen.SPLASH,
    Screen.FEEDBACK,
    Screen.MAIN_MENU,
    Screen.NEW_GAME,
    Screen.STELLAR_EXPLORER,
    Screen.SCORE,
    Screen.ACHIEVEMENT,
    Screen.CREDIT -> arrayOf(
        "tracks/ville_seppanen-1_g.mp3",
    )

    Screen.TUTORIAL,
    Screen.GAME,
    Screen.EVENT -> arrayOf(
        "tracks/blind_shift.mp3",
        "tracks/graduality.mp3",
        "tracks/led_twilight.mp3",
        "tracks/my_very_own_dead_ship.mp3",
        "tracks/neon_sky.mp3",
        "tracks/rain_in_space.mp3",
        "tracks/space_gras.mp3",
    )

    Screen.GAME_OVER -> arrayOf(
        "tracks/space.mp3",
    )
}
