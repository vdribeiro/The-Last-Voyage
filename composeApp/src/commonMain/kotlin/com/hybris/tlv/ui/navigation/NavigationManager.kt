package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

internal interface NavigationManager {

    enum class Screen {
        ERROR,
        SPLASH,
        MAIN_MENU,
        NEW_GAME,
        GAME,
        EVENT,
        GAME_OVER,
        LEARN,
        STELLAR_EXPLORER,
        SCORE,
        ACHIEVEMENT,
        CREDIT,
    }

    data class State(
        val music: Boolean = true,
        val screen: Screen = Screen.SPLASH,
        val state: Any? = null
    )

    val stateFlow: StateFlow<State>

    fun navigate(screen: Screen, state: Any? = null)

    fun setMusic(enabled: Boolean)

    @Composable
    fun Screen(
        screen: Screen,
        state: Any?
    )
}
