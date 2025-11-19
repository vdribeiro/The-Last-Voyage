package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import com.hybris.tlv.ui.store.Store

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

/**
 * Adds to the [NavGraphBuilder] a [screen] composable with its [store],
 * and sets the [Back] and forward navigation, with the latter replacing the existing screen if it is already in the stack.
 */
internal inline fun <reified S: Screen, State, Action> NavGraphBuilder.graph(
    crossinline store: (S) -> Store<State, Action>,
    crossinline screen: @Composable (Store<State, Action>) -> Unit,
) {
    composable<S> { entry ->
        val navController = rememberNavController()
        val args = entry.toRoute<S>()
        val store = viewModel { store(args) }
        LifecycleCoroutine(store) {
            store.effect.collect { screen ->
                when (screen) {
                    Back -> navController.popBackStack()
                    else -> navController.navigate(route = screen) { popUpTo<S> { inclusive = true } }
                }
            }
        }
        screen(store)
    }
}
