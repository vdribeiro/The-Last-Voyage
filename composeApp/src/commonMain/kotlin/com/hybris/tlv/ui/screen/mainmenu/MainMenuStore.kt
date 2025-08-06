package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases

internal sealed interface MainMenuAction {
    data object NewGame: MainMenuAction
    data object Continue: MainMenuAction
    data object StellarExplorer: MainMenuAction
    data object Explore: MainMenuAction
    data object Scores: MainMenuAction
    data object Achievements: MainMenuAction
    data object Credits: MainMenuAction
}

internal data class MainMenuState(
    val ongoingGameSession: Boolean? = null
)

internal class MainMenuStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: MainMenuState,
    private val gameSessionUseCases: GameSessionUseCases
): Store<MainMenuAction, MainMenuState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        val ongoingGameSession = gameSession != null &&
                gameSession.settledPlanetId == null &&
                gameSession.finalHabitability == null &&
                gameSession.integrity > 0 &&
                gameSession.fuel > 0
        updateState { it.copy(ongoingGameSession = ongoingGameSession) }
    }

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.NewGame -> navigate(screen = Navigation.Screen.NEW_GAME)
            MainMenuAction.Continue -> navigate(screen = Navigation.Screen.GAME)
            MainMenuAction.Explore -> navigate(screen = Navigation.Screen.EXPLORE)
            MainMenuAction.StellarExplorer -> navigate(screen = Navigation.Screen.STELLAR_EXPLORER)
            MainMenuAction.Scores -> navigate(screen = Navigation.Screen.SCORES)
            MainMenuAction.Achievements -> navigate(screen = Navigation.Screen.ACHIEVEMENTS)
            MainMenuAction.Credits -> navigate(screen = Navigation.Screen.CREDITS)
        }
    }
}
