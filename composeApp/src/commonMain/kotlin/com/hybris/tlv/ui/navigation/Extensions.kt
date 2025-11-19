package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.ui.store.Store

internal interface Screen
@Serializable
internal data object Back: Screen

/**
 * Adds to the [NavGraphBuilder] a [screen] composable with its [store], and sets the [Back] and forward navigation,
 * with the latter replacing the existing screen if it is already in the stack.
 */
@OptIn(ExperimentalComposeUiApi::class)
internal inline fun <reified S: Screen, reified T: Store<*, *>> NavGraphBuilder.graph(
    navController: NavHostController,
    crossinline store: (S) -> T,
    crossinline screen: @Composable (T) -> Unit,
) = composable<S> { entry ->
    val args = entry.toRoute<S>()
    val store = viewModel { store(args) }
    LaunchedEffect(key1 = store) {
        store.effect.collect { screen ->
            when (screen) {
                Back -> navController.popBackStack()
                else -> {
                    val existingEntry = navController.currentBackStack.value
                        .lastOrNull { it.destination.hasRoute(route = screen::class) }
                    navController.navigate(route = screen) {
                        if (existingEntry != null) popUpTo(route = existingEntry.destination.id) { inclusive = true }
                    }
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().backNavigation { store.back() }) { screen(store) }
}
