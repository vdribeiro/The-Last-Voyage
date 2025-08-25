package com.hybris.tlv.ui.screen.learn

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store

internal sealed interface LearnAction {
    data object Back: LearnAction
    data object Mechanics: LearnAction
    data object Habitability: LearnAction
    data object PlanetTypes: LearnAction
}

internal data class LearnState(
    val currentContent: Content? = null,
)

internal enum class Content {
    MENU,
    MECHANICS,
    HABITABILITY,
    PLANET_TYPES,
}

internal class LearnStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: LearnState,
): Store<LearnAction, LearnState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        updateState { it.copy(currentContent = Content.MENU) }
    }

    override fun reducer(state: LearnState, action: LearnAction) {
        when (action) {
            LearnAction.Back -> when (state.currentContent) {
                null, Content.MENU -> navigate(screen = Screen.MAIN_MENU)
                Content.MECHANICS, Content.HABITABILITY, Content.PLANET_TYPES -> updateState { it.copy(currentContent = Content.MENU) }
            }

            LearnAction.Mechanics -> updateState {
                it.copy(currentContent = Content.MECHANICS)
            }

            LearnAction.Habitability -> updateState {
                it.copy(currentContent = Content.HABITABILITY)
            }

            LearnAction.PlanetTypes -> updateState {
                it.copy(currentContent = Content.PLANET_TYPES)
            }
        }
    }
}
