package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.ui.navigation.Navigation.State
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
        EXPLORE,
        STELLAR_EXPLORER,
        SCORE,
        ACHIEVEMENT,
        CREDIT,
    }

    val stateFlow: StateFlow<State>

    fun navigate(screen: Screen, state: Any? = null)

    fun setMusic(enabled: Boolean)

    @Composable
    fun Screen(
        screen: Screen,
        state: Any?
    )
}
