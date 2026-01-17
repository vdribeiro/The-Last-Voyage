package com.hybris.tlv.ui.navigation

import kotlin.reflect.KType
import kotlin.reflect.typeOf
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.serializer.decode
import com.hybris.tlv.data.serializer.decodeURL
import com.hybris.tlv.data.serializer.encode
import com.hybris.tlv.data.serializer.encodeURL
import com.hybris.tlv.test.ExcludeFromTesting

/**
 * Navigate to the given [screen].
 * If the screen is not in the stack, add it to end of the stack.
 * If the screen is already in the stack, replace it and truncate onwards.
 */
internal inline fun <reified S: Screen> NavHostController.navigate(screen: S) = runCatching {
    Telemetry.info(tag = TAG, message = "Navigating to: $screen")
    currentBackStack.value.find { it.destination.hasRoute(route = screen::class) }?.destination?.route?.let { popBackStack(route = it, inclusive = true) }
    navigate(route = screen)
    Telemetry.info(tag = TAG, message = "Navigation stack: ${printBackStack()}")
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to navigate to screen $screen", throwable = it) }.getOrDefault(defaultValue = Unit)

/**
 * Pop to the previous destination.
 */
internal fun NavHostController.back(): Boolean = runCatching {
    Telemetry.info(tag = TAG, message = "Navigating back")
    popBackStack().also { Telemetry.info(tag = TAG, message = "Navigation stack: ${printBackStack()}") }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to go back", throwable = it) }.getOrDefault(defaultValue = false)

/**
 * Prints the current navigation back stack in a reader-friendly format.
 */
private fun NavHostController.printBackStack(): String = runCatching {
    currentBackStack.value
        .mapNotNull { it.destination.route }
        .map { route ->
            route.substringAfterLast(delimiter = ".")
                .substringBefore(delimiter = "$")
                .substringBefore(delimiter = "?")
        }
        .filter { it.isNotBlank() }
        .joinToString(separator = " -> ")
}.onFailure { Telemetry.error(tag = TAG, message = "Error printing backstack", throwable = it) }.getOrDefault(defaultValue = "")

/**
 * Creates a map of destination arguments with a NavType for a serializable object of type [T].
 */
internal inline fun <reified T> typeMapOf(): Map<KType, NavType<T?>> =
    mapOf(pair = typeOf<T?>() to serializableType<T?>())

@ExcludeFromTesting
private inline fun <reified T> serializableType(): NavType<T> =
    object: NavType<T>(isNullableAllowed = true) {
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

private const val TAG = "Routing"
