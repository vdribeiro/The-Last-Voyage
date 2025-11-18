package com.hybris.tlv.ui.navigation

/**
 * All possible screens in the app.
 */
internal sealed interface Route {
    data object Splash: Route
    data object MainMenu: Route
    data object Help: Route
    data object Feedback: Route
    data object NewGame: Route
    data object Tutorial: Route
    data object Game: Route
    data object Event: Route
    data object GameOver: Route
    data object StellarExplorer: Route
    data object Score: Route
    data object Achievement: Route
    data object Credit: Route
}
