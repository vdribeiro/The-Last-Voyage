package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.storage.Config
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
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
    val ongoingGameSession: Boolean = false,
    val developerCorner: String? = null,
    val tip: String? = null
)

internal class MainMenuStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: MainMenuState,
    private val remoteConfig: RemoteConfig,
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
        val developerCorner = remoteConfig.getString(key = Config.DeveloperCorner)
        val tip = remoteConfig.getString(key = Config.Tip)
        updateState {
            it.copy(
                ongoingGameSession = ongoingGameSession,
                developerCorner = developerCorner,
                tip = tip
            )
        }
    }

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.NewGame -> navigate(screen = Screen.NEW_GAME)
            MainMenuAction.Continue -> navigate(screen = Screen.GAME)
            MainMenuAction.Explore -> navigate(screen = Screen.EXPLORE)
            MainMenuAction.StellarExplorer -> navigate(screen = Screen.STELLAR_EXPLORER)
            MainMenuAction.Scores -> navigate(screen = Screen.SCORE)
            MainMenuAction.Achievements -> navigate(screen = Screen.ACHIEVEMENT)
            MainMenuAction.Credits -> navigate(screen = Screen.CREDIT)
        }
    }
}
