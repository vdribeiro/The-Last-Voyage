package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigation manager with the screens index.
 */
internal interface NavigationManager {
    data class NavigationState(
        val screen: Screen = Screen.SPLASH,
        val state: Any? = null
    )

    val stateFlow: StateFlow<NavigationState>

    var back: () -> Unit

    fun navigate(screen: Screen, state: Any? = null) {}

    @Composable
    fun Screen(navigationState: NavigationState) {
    }

    enum class Screen {
        SPLASH,
        MAIN_MENU,
        FEEDBACK,
        NEW_GAME,
        TUTORIAL,
        GAME,
        EVENT,
        GAME_OVER,
        STELLAR_EXPLORER,
        SCORE,
        ACHIEVEMENT,
        CREDIT;
    }
}
