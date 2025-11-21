package com.hybris.tlv.ui.navigation

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
        tutorialScreen()
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
 * If it is already in the stack, replace the existing one and truncate onwards.
 */
internal inline fun <reified S: Screen> NavHostController.navigate(screen: S) {
    Telemetry.info(tag = TAG, message = "Navigating to: $screen")
    navigate(route = screen) { popUpTo(route = S::class) { inclusive = true } }
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
