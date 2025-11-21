package com.hybris.tlv.ui.navigation

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.hybris.tlv.serializer.decode
import com.hybris.tlv.serializer.decodeURL
import com.hybris.tlv.serializer.encode
import com.hybris.tlv.serializer.encodeURL
import com.hybris.tlv.telemetry.Telemetry

/**
 * Navigate to the given [screen].
 * If it is already in the stack, replace the existing one and truncate onwards.
 */
internal fun NavHostController.navigate(screen: Screen) {
    Telemetry.info(tag = TAG, message = "Navigating to: $screen")
    val currentBackStack = currentBackStack.value
    val existingEntry = currentBackStack.lastOrNull { it.destination.hasRoute(route = screen::class) }
    navigate(route = screen) { if (existingEntry != null) popUpTo(route = screen) { inclusive = true } }
}

/**
 * Creates a NavType for a serializable object of type [T].
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

private const val TAG = "Navigation"
