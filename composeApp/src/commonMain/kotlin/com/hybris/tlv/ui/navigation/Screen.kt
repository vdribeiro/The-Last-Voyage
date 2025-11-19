package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder

/**
 * All possible screens in the app.
 */
@Serializable
internal data object SplashScreen: Screen
@Serializable
internal data object MainMenuScreen: Screen
@Serializable
internal data object HelpScreen: Screen
@Serializable
internal data class FeedbackScreen(val stateBuilder: FeedbackStateBuilder): Screen
@Serializable
internal data object NewGameScreen: Screen
@Serializable
internal data class TutorialScreen(val stateBuilder: TutorialStateBuilder = TutorialStateBuilder.Default(newGame = false)): Screen
@Serializable
internal data class GameScreen(val stateBuilder: GameStateBuilder = GameStateBuilder.Default): Screen
@Serializable
internal data object GameOverScreen: Screen
@Serializable
internal data object StellarExplorerScreen: Screen
@Serializable
internal data object ScoreScreen: Screen
@Serializable
internal data object AchievementScreen: Screen
@Serializable
internal data object CreditScreen: Screen

@Serializable
internal data object Back: Screen
internal interface Screen
