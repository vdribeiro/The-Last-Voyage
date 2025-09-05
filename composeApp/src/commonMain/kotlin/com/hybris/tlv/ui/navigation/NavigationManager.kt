package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigation manager with the screens index.
 */
internal interface NavigationManager {

    enum class Screen {
        FEEDBACK,
        SPLASH,
        MAIN_MENU,
        NEW_GAME,
        GAME,
        EVENT,
        GAME_OVER,
        STELLAR_EXPLORER,
        SCORE,
        ACHIEVEMENT,
        CREDIT,
    }

    data class State(
        val screen: Screen = Screen.SPLASH,
        val state: Any? = null
    )

    val stateFlow: StateFlow<State>

    var back: () -> Unit

    fun navigate(screen: Screen, state: Any? = null)

    @Composable
    fun Screen(
        screen: Screen,
        state: Any?
    )
}
