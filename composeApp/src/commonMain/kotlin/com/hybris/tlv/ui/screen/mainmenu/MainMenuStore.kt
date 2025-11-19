package com.hybris.tlv.ui.screen.mainmenu

import kotlinx.coroutines.Job
import com.hybris.tlv.cheats
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.platform.Property
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.AchievementScreen
import com.hybris.tlv.ui.navigation.CreditScreen
import com.hybris.tlv.ui.navigation.GameScreen
import com.hybris.tlv.ui.navigation.NewGameScreen
import com.hybris.tlv.ui.navigation.ScoreScreen
import com.hybris.tlv.ui.navigation.StellarExplorerScreen
import com.hybris.tlv.ui.navigation.TutorialScreen
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
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

    private fun setup() {
        launch {
            config.preferences.collect { preferences ->
                val cheatsEnabled = preferences.cheats
                updateState { it.copy(cheatsEnabled = cheatsEnabled) }
            }
        }
        launch {
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
    }

    private fun newGame(): Job = launch {
        Telemetry.info(tag = TAG, message = "New game")
        if (config.preferences.value.showTutorial) updateState { it.copy(newGameDialog = true) } else navigate(screen = NewGameScreen)
    }

    private fun newGameWithoutTutorial(): Job = launch {
        config.setPreferences { it.copy(showTutorial = false) }.savePreferences()
        navigate(screen = NewGameScreen)
    }

    private fun newGameWithTutorial(): Job = launch {
        Telemetry.info(tag = TAG, message = "Show tutorial")
        config.setPreferences { it.copy(showTutorial = false) }.savePreferences()
        navigate(screen = TutorialScreen(stateBuilder = TutorialStateBuilder.Default(newGame = true)))
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
            MainMenuAction.Next -> navigate(screen = GameScreen())
            MainMenuAction.Scores -> navigate(screen = ScoreScreen)
            MainMenuAction.Achievements -> navigate(screen = AchievementScreen)
            MainMenuAction.Credits -> navigate(screen = CreditScreen)
            MainMenuAction.StellarExplorer -> navigate(screen = StellarExplorerScreen)
            MainMenuAction.DisableCheats -> disableCheats()
        }
    }

    companion object {
        private const val TAG = "MainMenuStore"
    }
}
