package com.hybris.tlv.ui.navigation

/**
 * All possible screens in the app.
 */
sealed interface Screen {
    data object Splash: Screen
    data object MainMenu: Screen
    data object Feedback: Screen
    data object NewGame: Screen
    data object Tutorial: Screen
    data object Game: Screen
    data object Event: Screen
    data object GameOver: Screen
    data object StellarExplorer: Screen
    data object Score: Screen
    data object Achievement: Screen
    data object Credit: Screen
}
