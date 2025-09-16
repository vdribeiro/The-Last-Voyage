package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigation manager with the screens index.
 */
internal interface NavigationManager {
    data class State(
        val screen: Screen = Screen.SPLASH,
        val stateBuilder: Any? = null
    )

    val stateFlow: StateFlow<State>

    var back: () -> Unit

    fun navigate(screen: Screen, stateBuilder: Any? = null)

    @Composable
    fun Screen(screen: Screen, stateBuilder: Any? = null)

    enum class Screen {
        SPLASH,
        MAIN_MENU,
        FEEDBACK,
        NEW_GAME,
        GAME,
        EVENT,
        GAME_OVER,
        STELLAR_EXPLORER,
        SCORE,
        ACHIEVEMENT,
        CREDIT;

        companion object {
            private val map = Screen.entries.associateBy(keySelector = Screen::name)
            fun fromValue(value: String): Screen = map[value.uppercase()] ?: MAIN_MENU
        }
    }
}
