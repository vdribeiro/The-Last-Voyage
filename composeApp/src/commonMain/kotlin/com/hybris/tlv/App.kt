package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.SplashScreen
import com.hybris.tlv.ui.navigation.eventGraph
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.UseCases

@Composable
internal fun App(
    modifier: Modifier = Modifier,
    useCases: UseCases,
    audioPlayer: AudioPlayer
) = AppTheme {
    // Setup Navigation
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashScreen
    ) {
        eventGraph(navController = navController, useCases = useCases)
    }

    // Setup Audio Player
//    LifecycleCoroutine(navController.currentDestination) {
//        val playlist = getTracks(screen = navController.currentDestination)
//        if (playlist != null) audioPlayer.action(action = Action.Play(playlist = playlist))
//    }
//    Register(
//        onBackground = { audioPlayer.action(action = Action.Pause) },
//        onForeground = { audioPlayer.action(action = Action.Resume) },
//    )
}

//
///**
// * A simple registry to handle "Back" overrides (Replaces Android's OnBackPressedDispatcher for KMP).
// * Allows screens to intercept the global back event.
// */
//class BackDispatcher {
//    // LIFO Stack: The most recently added handler gets priority.
//    private val handlers = ArrayDeque<() -> Unit>()
//
//    fun register(handler: () -> Unit) {
//        handlers.addLast(handler)
//    }
//
//    fun unregister(handler: () -> Unit) {
//        handlers.remove(handler)
//    }
//
//    /**
//     * Dispatches the back event to the most recently registered handler.
//     * Returns true if handled (intercepted), false if no handlers were registered.
//     */
//    fun dispatch(): Boolean {
//        val handler = handlers.lastOrNull()
//        if (handler != null) {
//            handler()
//            return true
//        }
//        return false
//    }
//}
//
///**
// * Global Provider to access the dispatcher anywhere in the widget tree.
// */
//val LocalBackDispatcher = staticCompositionLocalOf { BackDispatcher() }
//
///**
// * Registers a callback to intercept the Back event.
// * Place this inside your Screens (e.g., HelpScreen) to override default behavior.
// *
// * @param enabled Whether this handler is currently active.
// * @param onBack The action to perform when back is requested.
// */
//@Composable
//fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
//    val dispatcher = LocalBackDispatcher.current
//
//    // Re-register if 'onBack' or 'enabled' changes
//    DisposableEffect(dispatcher, enabled, onBack) {
//        val handler = onBack
//        if (enabled) {
//            dispatcher.register(handler)
//        }
//        onDispose {
//            dispatcher.unregister(handler)
//        }
//    }
//}
///**
// * Navigates to a route, replacing the existing one if it's already in the stack.
// */
//inline fun <reified T : Any> NavController.navigateSingleStack(route: T) {
//    navigate(route) {
//        popUpTo<T> { inclusive = true }
//    }
//}