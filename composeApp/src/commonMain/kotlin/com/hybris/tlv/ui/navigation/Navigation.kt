package com.hybris.tlv.ui.navigation

import kotlin.reflect.KType
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.hybris.tlv.Dependency
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.serializer.decode
import com.hybris.tlv.serializer.decodeURL
import com.hybris.tlv.serializer.encode
import com.hybris.tlv.serializer.encodeURL
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.graph.achievementScreen
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
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.UseCases

/**
 * Set the Navigation graph.
 */
@Composable
internal fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases,
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
        splashScreen(navController = navController, config = config, useCases = useCases)
        mainMenuScreen(navController = navController, config = config, useCases = useCases)
        helpScreen(navController = navController, config = config, useCases = useCases)
        feedbackScreen(navController = navController)
        newGameScreen(navController = navController, useCases = useCases)
        tutorialScreen(navController = navController)
        gameScreen(navController = navController, useCases = useCases)
        eventScreen(navController = navController, useCases = useCases)
        gameOverScreen(navController = navController, useCases = useCases)
        stellarExplorerScreen(navController = navController, useCases = useCases)
        scoreScreen(navController = navController, useCases = useCases)
        achievementScreen(navController = navController, useCases = useCases)
        creditScreen(navController = navController, useCases = useCases)
    }
}

/**
 * Adds to the [NavGraphBuilder] a [screen] composable with its [store], and sets the back and forward navigation,
 * with the latter replacing the existing screen if it is already in the stack.
 */
@OptIn(ExperimentalComposeUiApi::class)
internal inline fun <reified S: Screen, reified T: Store<*, *>> NavGraphBuilder.graph(
    navController: NavHostController,
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    crossinline store: (S) -> T,
    crossinline screen: @Composable (T) -> Unit,
    crossinline block: () -> Unit = {}
) = composable<S>(typeMap = typeMap) { entry ->
    val args = entry.toRoute<S>()
    val store = viewModel { store(args) }
    LaunchedEffect(key1 = store) {
        store.navigation.collect { screen ->
            Telemetry.info(tag = TAG, message = "Navigating to: $screen")
            val currentBackStack = navController.currentBackStack.value
            val existingEntry = currentBackStack.lastOrNull { it.destination.hasRoute(route = screen::class) }
            navController.navigate(route = screen) { if (existingEntry != null) popUpTo(route = screen) { inclusive = true } }
        }
    }
    LaunchedEffect(key1 = store) {
        store.action.collect { action ->
            when (action) {
                Action.Back -> navController.popBackStack()
                Action.ToggleAudio -> {}//dependency.audioPlayer.action(action = AudioPlayer.Action.Toggle)
                Action.DisableCheats -> {}//dependency.config.setPreferences { it.copy(cheats = false) }
            }
        }
    }
    block()
    screen(store)
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
