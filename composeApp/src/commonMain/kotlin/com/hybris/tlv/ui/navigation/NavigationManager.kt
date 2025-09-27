package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigation manager with the screens index.
 */
internal interface NavigationManager {
    /**
     * Current navigation destination.
     */
    data class NavigationState(
        val screen: Screen = Screen.Splash,
        val stateBuilder: Any? = null
    )

    /**
     * The flow of navigation states collected by the App.
     */
    val stateFlow: StateFlow<NavigationState>

    /**
     * A callback for the back action, handled by the App's [androidx.compose.ui.backhandler.BackHandler]
     * and implemented optionally in the current screen's Store.
     */
    var back: () -> Unit

    /**
     * Navigates to a new screen.
     */
    fun navigate(screen: Screen, state: Any? = null) {}

    /**
     * The main composable responsible for rendering the current screen based on the navigation state.
     */
    @Composable
    fun Screen(navigationState: NavigationState) {
    }

    /**
     * All possible screens in the app.
     */
    sealed interface Screen {
        data object Splash: Screen
        data object MainMenu: Screen
        data object Feedback: Screen
        data object NewGame: Screen
        data object Tutorial: Screen
        data object Game: Screen
        data object Event: Screen
        data object GameOver: Screen
        data object StellarExplorer: Screen
        data object Score: Screen
        data object Achievement: Screen
        data object Credit: Screen
    }
}
