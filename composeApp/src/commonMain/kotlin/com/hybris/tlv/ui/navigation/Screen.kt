package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import com.hybris.tlv.domain.usecase.ship.model.Ship

/**
 * All possible screens in the application.
 * This is used by the navigation component to define the different destinations.
 * Each object or data class represents a distinct screen and can carry arguments required by that screen.
 * All screens must be [Serializable] to support being passed as navigation arguments.
 */
@Serializable
internal sealed interface Screen {
    @Serializable
    data class Splash(val reset: Boolean): Screen
    @Serializable
    data object Cheat: Screen
    @Serializable
    data object MainMenu: Screen
    @Serializable
    data object Help: Screen
    @Serializable
    data class Feedback(val tag: String?, val message: String?): Screen
    @Serializable
    data object NewGame: Screen
    @Serializable
    data class Tutorial(val newGame: Boolean): Screen
    @Serializable
    data object Catastrophe: Screen
    @Serializable
    data class Game(val ship: Ship?): Screen
    @Serializable
    data class Event(val ship: Ship?): Screen
    @Serializable
    data object GameOver: Screen
    @Serializable
    data object StellarExplorer: Screen
    @Serializable
    data object Score: Screen
    @Serializable
    data object Achievement: Screen
    @Serializable
    data object Credit: Screen
    @Serializable
    data object CatastropheExplorer: Screen
    @Serializable
    data object EventExplorer: Screen
}
