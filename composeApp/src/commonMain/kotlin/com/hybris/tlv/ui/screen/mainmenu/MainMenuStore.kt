package com.hybris.tlv.ui.screen.mainmenu

import kotlinx.coroutines.Job
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.platform.Property
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases

internal class MainMenuStore(
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: MainMenuStateBuilder,
    private val config: ConfigManager,
    private val gameSessionUseCases: GameSessionUseCases,
): Store<MainMenuState, MainMenuAction>(
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        MainMenuStateBuilder.Default -> MainMenuState()
        is MainMenuStateBuilder.FromState -> stateBuilder.state
    }
) {
    init {
        when (stateBuilder) {
            MainMenuStateBuilder.Default -> setup()
            is MainMenuStateBuilder.FromState -> {}
        }
    }

    override fun getSavableState(state: MainMenuState): Any =
        MainMenuStateBuilder.FromState(state = state.copy(newGameDialog = false))

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val preferences = config.preferences
        val configs = config.remoteConfigs
        val newVersionBanner = Property.APP_VERSION_NUMBER < configs.appVersion
        val showNavigationInfo = preferences.showNavigationInfo
        val developerCorner = configs.developerCorner
        val support = configs.support
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        updateState {
            it.copy(
                loading = false,
                newVersionBanner = newVersionBanner,
                showNavigationInfo = showNavigationInfo,
                developerCorner = developerCorner,
                support = support,
                ongoingGameSession = ongoingGameSession,
            )
        }
        config.setPreferences { it.copy(showNavigationInfo = false) }.savePreferences()
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun newGame(): Job = launch {
        Telemetry.info(tag = TAG, message = "New game")
        if (config.preferences.showTutorial) updateState { it.copy(newGameDialog = true) } else navigate(screen = Screen.NewGame)
    }

    private fun newGameWithoutTutorial(): Job = launch {
        config.setPreferences { it.copy(showTutorial = false) }.savePreferences()
        navigate(screen = Screen.NewGame)
    }

    private fun newGameWithTutorial(): Job = launch {
        Telemetry.info(tag = TAG, message = "Show tutorial")
        config.setPreferences { it.copy(showTutorial = false) }.savePreferences()
        navigate(screen = Screen.Tutorial, stateBuilder = TutorialStateBuilder.Default(newGame = true))
    }

    override fun goBack(state: MainMenuState) {}

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.NewGame -> newGame()
            MainMenuAction.HideNavigationInfo -> updateState { it.copy(showNavigationInfo = false) }
            MainMenuAction.HideNewGameDialog -> updateState { it.copy(newGameDialog = false) }
            MainMenuAction.NoNewGameDialog -> newGameWithoutTutorial()
            MainMenuAction.YesNewGameDialog -> newGameWithTutorial()
            MainMenuAction.Next -> navigate(screen = Screen.Game)
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
