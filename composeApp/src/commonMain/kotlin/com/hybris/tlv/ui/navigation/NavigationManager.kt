package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.backhandler.BackHandler
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigation manager with the screens index.
 */
internal interface NavigationManager {
    /**
     * Current navigation state.
     */
    data class NavigationState(
        val screen: Screen = Screen.Splash,
        val stateBuilder: Any? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NavigationState) return false
            return screen == other.screen
        }

        override fun hashCode(): Int = screen.hashCode()
    }

    val stateFlow: StateFlow<NavigationState>

    /**
     * A callback for the back action, handled by the App's [BackHandler].
     */
    var back: () -> Unit

    /**
     * Goes back to the previous screen.
     */
    fun goBack()

    /**
     * Updates the state of the current navigation and then navigates to a new screen given a new state.
     */
    fun navigate(screen: Screen, stateBuilder: Any? = null, savableState: Any? = null)

    /**
     * The main composable responsible for rendering the current screen based on the navigation state.
     */
    @Composable
    fun Screen(navigationState: NavigationState)
}
