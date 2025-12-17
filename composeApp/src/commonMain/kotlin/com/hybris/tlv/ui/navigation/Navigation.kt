package com.hybris.tlv.ui.navigation

import kotlin.reflect.KType
import kotlin.reflect.typeOf
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.serializer.decode
import com.hybris.tlv.serializer.decodeURL
import com.hybris.tlv.serializer.encode
import com.hybris.tlv.serializer.encodeURL
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.graph.achievementScreen
import com.hybris.tlv.ui.navigation.graph.cheatScreen
import com.hybris.tlv.ui.navigation.graph.creditScreen
import com.hybris.tlv.ui.navigation.graph.eventScreen
import com.hybris.tlv.ui.navigation.graph.feedbackScreen
import com.hybris.tlv.ui.navigation.graph.gameOverScreen
import com.hybris.tlv.ui.navigation.graph.gameScreen
import com.hybris.tlv.ui.navigation.graph.helpScreen
import com.hybris.tlv.ui.navigation.graph.mainMenuScreen
import com.hybris.tlv.ui.navigation.graph.newGameScreen
import com.hybris.tlv.ui.navigation.graph.scoreScreen
import com.hybris.tlv.ui.navigation.graph.splashScreen
import com.hybris.tlv.ui.navigation.graph.stellarExplorerScreen
import com.hybris.tlv.ui.navigation.graph.tutorialScreen
import com.hybris.tlv.usecase.UseCases

/**
 * The main navigation host for the application.
 * This composable sets up the [NavHost] and defines all the possible navigation destinations within the app,
 * linking each [Screen] to its corresponding composable content.
 */
@Composable
internal fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Splash,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        splashScreen(config = config, useCases = useCases)
        cheatScreen(config = config)
        mainMenuScreen(config = config, useCases = useCases)
        helpScreen(config = config, useCases = useCases)
        feedbackScreen()
        newGameScreen(useCases = useCases)
        tutorialScreen(config = config)
        gameScreen(useCases = useCases)
        eventScreen(useCases = useCases)
        gameOverScreen(useCases = useCases)
        stellarExplorerScreen(useCases = useCases)
        scoreScreen(useCases = useCases)
        achievementScreen(useCases = useCases)
        creditScreen(useCases = useCases)
    }
}

/**
 * Navigate to the given [screen].
 * If the screen is not in the stack, add it to end of the stack.
 * If the screen is already in the stack, eiter replace or restore it and truncate onwards.
 */
internal inline fun <reified S: Screen> NavHostController.navigate(screen: S, restore: Boolean) {
    Telemetry.info(tag = TAG, message = "Navigating to: $screen")
    navigate(route = screen) {
        popUpTo(route = S::class) { inclusive = !restore }
        launchSingleTop = restore
    }
    Telemetry.info(tag = TAG, message = "Navigation stack: ${printBackStack()}")
}

/**
 * Prints the current navigation back stack in a reader-friendly format.
 */
private fun NavHostController.printBackStack() = runCatching {
    currentBackStack.value.joinToString { it.destination.toString().substringAfterLast(delimiter = ".") }.substringAfter(delimiter = ",").trim()
}.onFailure { Telemetry.error(tag = TAG, message = "Error printing backstack", throwable = it) }.getOrDefault(defaultValue = "")

/**
 * Creates a map of destination arguments with a NavType for a serializable object of type [T].
 */
internal inline fun <reified T> typeMapOf(): Map<KType, NavType<T?>> =
    mapOf(pair = typeOf<T?>() to serializableType<T?>())

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

private const val TAG = "Navigation"
