package com.hybris.tlv.ui.screen.learn

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store

internal sealed interface LearnAction {
    data object Back: LearnAction
    data object StellarExplorer: LearnAction
    data object HostTypes: LearnAction
    data object PlanetTypes: LearnAction
    data object Properties: LearnAction
    data object Mechanics: LearnAction
    data object Habitability: LearnAction
}

internal data class LearnState(
    val currentContent: Content? = null,
)

internal enum class Content {
    MENU,
    HOST_TYPES,
    PLANET_TYPES,
    PROPERTIES,
    MECHANICS,
    HABITABILITY,
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
                Content.HOST_TYPES,
                Content.PLANET_TYPES,
                Content.PROPERTIES,
                Content.MECHANICS,
                Content.HABITABILITY -> updateState { it.copy(currentContent = Content.MENU) }
            }

            LearnAction.StellarExplorer -> navigate(screen = Screen.STELLAR_EXPLORER)

            LearnAction.HostTypes -> updateState {
                it.copy(currentContent = Content.HOST_TYPES)
            }

            LearnAction.PlanetTypes -> updateState {
                it.copy(currentContent = Content.PLANET_TYPES)
            }

            LearnAction.Properties -> updateState {
                it.copy(currentContent = Content.PROPERTIES)
            }

            LearnAction.Mechanics -> updateState {
                it.copy(currentContent = Content.MECHANICS)
            }

            LearnAction.Habitability -> updateState {
                it.copy(currentContent = Content.HABITABILITY)
            }
        }
    }
}
