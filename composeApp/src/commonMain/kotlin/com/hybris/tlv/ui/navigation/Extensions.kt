package com.hybris.tlv.ui.navigation

import kotlin.reflect.KType
import kotlinx.serialization.Serializable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.hybris.tlv.serializer.decode
import com.hybris.tlv.serializer.decodeURL
import com.hybris.tlv.serializer.encode
import com.hybris.tlv.serializer.encodeURL
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.store.Store

private const val TAG = "Navigation"

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
    typeMap: Map<KType, NavType<*>> = emptyMap(),
) = composable<S>(typeMap = typeMap) { entry ->
    val args = entry.toRoute<S>()
    val store = viewModel { store(args) }
    LaunchedEffect(key1 = store) {
        store.effect.collect { screen ->
            Telemetry.info(tag = TAG, message = "Navigating to: $screen")
            when (screen) {
                Back -> navController.popBackStack()
                else -> {
                    val currentBackStack = navController.currentBackStack.value
                    val existingEntry = currentBackStack.lastOrNull { it.destination.hasRoute(route = screen::class) }
                    navController.navigate(route = screen) { if (existingEntry != null) popUpTo(route = screen) { inclusive = true } }
                }
            }
            Telemetry.info(tag = TAG, message = "Navigation stack: ${navController.currentBackStack.value}")
        }
    }
    screen(store)
}

/**
 * Creates a NavType for a @Serializable object of type [T].
 */
internal inline fun <reified T> serializableType(): NavType<T> {
    return object: NavType<T>(isNullableAllowed = true) {
        override fun put(bundle: SavedState, key: String, value: T) {
            encode(value = value)?.let { bundle.write { putString(key = key, value = it) } }
        }

        override fun get(bundle: SavedState, key: String): T? =
            bundle.read { getStringOrNull(key = key)?.let { decode(value = it) } }

        override fun serializeAsValue(value: T): String =
            encodeURL(value = value)

        override fun parseValue(value: String): T =
            decodeURL<T>(value = value) as T
    }
}
