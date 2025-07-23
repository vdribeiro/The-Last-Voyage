package com.hybris.tlv.ui.screen.explore

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store

internal sealed interface ExploreAction {
    data object Back: ExploreAction
    data object StellarExplorer: ExploreAction
    data object Mechanics: ExploreAction
    data object Habitability: ExploreAction
}

internal data class ExploreState(
    val currentContent: Content? = null,
)

internal enum class Content {
    MENU,
    MECHANICS,
    HABITABILITY,
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
                Content.MECHANICS, Content.HABITABILITY -> updateState { it.copy(currentContent = Content.MENU) }
            }

            ExploreAction.StellarExplorer -> navigate(screen = Navigation.Screen.STELLAR_EXPLORER)

            ExploreAction.Mechanics -> updateState {
                it.copy(currentContent = Content.MECHANICS)
            }

            ExploreAction.Habitability -> updateState {
                it.copy(currentContent = Content.HABITABILITY)
            }
        }
    }
}
