package com.hybris.tlv.ui.screen.explore

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store

internal sealed interface ExploreAction {
    data object Back: ExploreAction
    data object Mechanics: ExploreAction
    data object Habitability: ExploreAction
    data object PlanetTypes: ExploreAction
}

internal data class ExploreState(
    val currentContent: Content? = null,
)

internal enum class Content {
    MENU,
    MECHANICS,
    HABITABILITY,
    PLANET_TYPES,
}

internal class ExploreStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: ExploreState,
): Store<ExploreAction, ExploreState>(
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

    override fun reducer(state: ExploreState, action: ExploreAction) {
        when (action) {
            ExploreAction.Back -> when (state.currentContent) {
                null, Content.MENU -> navigate(screen = Navigation.Screen.MAIN_MENU)
                Content.MECHANICS, Content.HABITABILITY, Content.PLANET_TYPES -> updateState { it.copy(currentContent = Content.MENU) }
            }

            ExploreAction.Mechanics -> updateState {
                it.copy(currentContent = Content.MECHANICS)
            }

            ExploreAction.Habitability -> updateState {
                it.copy(currentContent = Content.HABITABILITY)
            }

            ExploreAction.PlanetTypes -> updateState {
                it.copy(currentContent = Content.PLANET_TYPES)
            }
        }
    }
}
