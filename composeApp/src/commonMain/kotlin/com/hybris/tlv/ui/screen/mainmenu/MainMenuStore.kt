package com.hybris.tlv.ui.screen.mainmenu

import kotlinx.coroutines.Job
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.platform.Property
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases

internal class MainMenuStore(
    private val config: ConfigManager,
    private val gameSessionUseCases: GameSessionUseCases,
): Store<MainMenuState, MainMenuAction>(
    initialState = MainMenuState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val configs = config.localConfigs.value
        val newVersionBanner = Property.APP_VERSION_NUMBER < configs.appVersion
        val developerCorner = configs.developerCorner
        val support = configs.support
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        updateState {
            it.copy(
                loading = false,
                newVersionBanner = newVersionBanner,
                developerCorner = developerCorner,
                support = support,
                ongoingGameSession = ongoingGameSession,
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun newGame() {
        Telemetry.info(tag = TAG, message = "New game")
        if (!config.preferences.value.showTutorial) navigate(screen = Screen.NewGame) else {
            navigate(screen = Screen.Tutorial(newGame = true))
        }
    }

    override fun back(state: MainMenuState) {}

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.NewGame -> newGame()
            MainMenuAction.Next -> navigate(screen = Screen.Game())
            MainMenuAction.Scores -> navigate(screen = Screen.Score)
            MainMenuAction.Achievements -> navigate(screen = Screen.Achievement)
            MainMenuAction.Credits -> navigate(screen = Screen.Credit)
            MainMenuAction.StellarExplorer -> navigate(screen = Screen.StellarExplorer)
        }
    }

    companion object {
        private const val TAG = "MainMenuStore"
    }
}
