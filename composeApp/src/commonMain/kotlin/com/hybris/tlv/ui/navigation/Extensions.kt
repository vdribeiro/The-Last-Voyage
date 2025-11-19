package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.ui.store.Store

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
 * Adds to the [NavGraphBuilder] a [screen] composable with its [store], and sets the [Back] and forward navigation,
 * with the latter replacing the existing screen if it is already in the stack.
 */
internal inline fun <reified S: Screen, reified T: Store<*, *>> NavGraphBuilder.graph(
    crossinline store: (S) -> T,
    crossinline screen: @Composable (T) -> Unit,
) = composable<S> { entry ->
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
