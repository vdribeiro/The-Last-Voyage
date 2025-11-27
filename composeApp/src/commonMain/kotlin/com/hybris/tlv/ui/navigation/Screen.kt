package com.hybris.tlv.ui.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.Serializable
import com.hybris.tlv.usecase.ship.model.Ship

@Serializable
internal sealed interface Screen {
    @Serializable
    data object Splash: Screen
    @Serializable
    data object Cheat: Screen
    @Serializable
    data object MainMenu: Screen
    @Serializable
    data object Help: Screen
    @Serializable
    data class Feedback(val tag: String? = null, val message: String? = null): Screen
    @Serializable
    data object NewGame: Screen
    @Serializable
    data class Tutorial(val newGame: Boolean = false): Screen
    @Serializable
    data class Game(val ship: Ship? = null): Screen
    @Serializable
    data class Event(val ship: Ship? = null): Screen
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
}

internal val navigationChannel: Channel<Screen> = Channel()
