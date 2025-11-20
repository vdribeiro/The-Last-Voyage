package com.hybris.tlv.media

import com.hybris.tlv.ui.navigation.AchievementScreen
import com.hybris.tlv.ui.navigation.CreditScreen
import com.hybris.tlv.ui.navigation.EventScreen
import com.hybris.tlv.ui.navigation.FeedbackScreen
import com.hybris.tlv.ui.navigation.GameOverScreen
import com.hybris.tlv.ui.navigation.GameScreen
import com.hybris.tlv.ui.navigation.HelpScreen
import com.hybris.tlv.ui.navigation.MainMenuScreen
import com.hybris.tlv.ui.navigation.NewGameScreen
import com.hybris.tlv.ui.navigation.ScoreScreen
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.SplashScreen
import com.hybris.tlv.ui.navigation.StellarExplorerScreen
import com.hybris.tlv.ui.navigation.TutorialScreen

internal fun getTracks(screen: Screen): List<String>? = when (screen) {
    SplashScreen,
    MainMenuScreen,
    NewGameScreen,
    StellarExplorerScreen,
    ScoreScreen,
    AchievementScreen,
    CreditScreen -> listOf(
        "tracks/ville_seppanen-1_g.mp3",
    )

    GameScreen,
    EventScreen -> listOf(
        "tracks/blind_shift.mp3",
        "tracks/graduality.mp3",
        "tracks/led_twilight.mp3",
        "tracks/my_very_own_dead_ship.mp3",
        "tracks/neon_sky.mp3",
        "tracks/rain_in_space.mp3",
        "tracks/space_gras.mp3",
    )

    GameOverScreen -> listOf(
        "tracks/space.mp3",
    )

    HelpScreen,
    FeedbackScreen,
    TutorialScreen -> null

    else -> null
}
