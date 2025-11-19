package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import com.hybris.tlv.ui.screen.event.EventStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder

/**
 * All possible screens in the app.
 */
internal sealed interface Screen {
    @Serializable
    data object Splash: Screen
    @Serializable
    data object MainMenu: Screen
    @Serializable
    data object Help: Screen
    @Serializable
    data class Feedback(val stateBuilder: FeedbackStateBuilder): Screen
    @Serializable
    data object NewGame: Screen
    @Serializable
    data class Tutorial(val stateBuilder: TutorialStateBuilder = TutorialStateBuilder.Default(newGame = false)): Screen
    @Serializable
    data class Game(val stateBuilder: GameStateBuilder = GameStateBuilder.Default): Screen
    @Serializable
    data class Event(val stateBuilder: EventStateBuilder = EventStateBuilder.Default): Screen
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
