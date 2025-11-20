package com.hybris.tlv.ui.screen.mainmenu

import kotlinx.coroutines.Job
import com.hybris.tlv.cheats
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.platform.Property
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Game
import com.hybris.tlv.ui.navigation.NewGame
import com.hybris.tlv.ui.navigation.Score
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.StellarExplorer
import com.hybris.tlv.ui.navigation.Tutorial
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases

internal class MainMenuStore(
    private val config: ConfigManager,
    private val gameSessionUseCases: GameSessionUseCases,
): Store<MainMenuState, MainMenuAction>(
    config = config,
    initialState = MainMenuState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
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
    }.also {
        launch {
            config.preferences.collect { preferences ->
                val cheatsEnabled = preferences.cheats
                updateState { it.copy(cheatsEnabled = cheatsEnabled) }
            }
        }
    }

    private fun newGame(): Job = launch {
        Telemetry.info(tag = TAG, message = "New game")
        if (config.preferences.value.showTutorial) updateState { it.copy(newGameDialog = true) } else navigate(screen = NewGame)
    }

    private fun newGameWithoutTutorial(): Job = launch {
        config.setPreferences { it.copy(showTutorial = false) }
        navigate(screen = NewGame)
    }

    private fun newGameWithTutorial(): Job = launch {
        Telemetry.info(tag = TAG, message = "Show tutorial")
        config.setPreferences { it.copy(showTutorial = false) }
        navigate(screen = Tutorial(newGame = true))
    }

    private fun disableCheats(): Job = launch {
        config.cheats(enabled = false)
    }

    override fun back(state: MainMenuState) {}

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.NewGame -> newGame()
            MainMenuAction.HideNewGameDialog -> updateState { it.copy(newGameDialog = false) }
            MainMenuAction.NoNewGameDialog -> newGameWithoutTutorial()
            MainMenuAction.YesNewGameDialog -> newGameWithTutorial()
            MainMenuAction.Next -> navigate(screen = Game())
            MainMenuAction.Scores -> navigate(screen = Score)
            MainMenuAction.Achievements -> navigate(screen = Screen.Achievement)
            MainMenuAction.Credits -> navigate(screen = Screen.Credit)
            MainMenuAction.StellarExplorer -> navigate(screen = StellarExplorer)
            MainMenuAction.DisableCheats -> disableCheats()
        }
    }

    companion object {
        private const val TAG = "MainMenuStore"
    }
}
