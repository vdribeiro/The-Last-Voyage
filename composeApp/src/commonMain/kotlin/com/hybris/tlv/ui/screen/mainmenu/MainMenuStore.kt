package com.hybris.tlv.ui.screen.mainmenu

import kotlinx.coroutines.Job
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases

internal class MainMenuStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: MainMenuStateBuilder,
    private val config: ConfigManager,
    private val gameSessionUseCases: GameSessionUseCases,
): Store<MainMenuState, MainMenuAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        MainMenuStateBuilder.Default -> MainMenuState()
        is MainMenuStateBuilder.FromSavableState -> stateBuilder.state
    }
) {
    @get:VisibleForTesting
    internal var featureTutorial: Boolean = true

    init {
        when (stateBuilder) {
            MainMenuStateBuilder.Default -> setup()
            is MainMenuStateBuilder.FromSavableState -> {
                featureTutorial = stateBuilder.featureTutorial
            }
        }
    }

    override fun getSavableState(state: MainMenuState): Any =
        MainMenuStateBuilder.FromSavableState(state = state.copy(newGameDialog = false), featureTutorial = featureTutorial)

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val showNavigationInfo = config.preferences.showNavigationInfo
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        this@MainMenuStore.featureTutorial = config.localConfigs.featureTutorial
        updateState {
            it.copy(
                loading = false,
                showNavigationInfo = showNavigationInfo,
                featureScores = config.localConfigs.featureScores,
                featureAchievements = config.localConfigs.featureAchievements,
                featureStellarExplorer = config.localConfigs.featureStellarExplorer,
                featureNewGame = config.localConfigs.featureNewGame,
                developerCorner = config.localConfigs.developerCorner,
                support = config.localConfigs.support,
                ongoingGameSession = ongoingGameSession,
            )
        }
        config.setPreferences { it.copy(showNavigationInfo = false) }.savePreferences()
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun newGame(): Job = launch {
        Telemetry.info(tag = TAG, message = "New game")
        if (featureTutorial && config.preferences.showTutorial) {
            updateState { it.copy(newGameDialog = true) }
        } else navigate(screen = Screen.NewGame)
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
