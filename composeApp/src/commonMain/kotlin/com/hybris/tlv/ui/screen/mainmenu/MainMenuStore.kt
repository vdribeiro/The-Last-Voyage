package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.learning.LearningUseCases
import kotlinx.coroutines.Job

internal class MainMenuStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: MainMenuStateBuilder,
    private val config: ConfigManager,
    private val gameSessionUseCases: GameSessionUseCases,
    private val learningUseCases: LearningUseCases
): Store<MainMenuState, MainMenuAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        MainMenuStateBuilder.Default -> MainMenuState()
        is MainMenuStateBuilder.FromSavableState -> MainMenuState(currentContent = stateBuilder.currentContent)
    }
) {
    init {
        setup()
    }

    override fun getSavableState(state: MainMenuState): Any? =
        MainMenuStateBuilder.FromSavableState(currentContent = state.currentContent)

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        config.fetch()
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        val learningsMap = learningUseCases.getLearnings().groupBy { it.type }

        updateState {
            it.copy(
                loading = false,
                featureLearn = config.localConfigs.featureLearn,
                featureScores = config.localConfigs.featureScores,
                featureAchievements = config.localConfigs.featureAchievements,
                featureStellarExplorer = config.localConfigs.featureStellarExplorer,
                featureNewGame = config.localConfigs.featureNewGame,
                featureTutorial = config.localConfigs.featureTutorial,
                developerCorner = config.localConfigs.developerCorner,
                support = config.localConfigs.support,
                formula = config.localConfigs.formula,
                ongoingGameSession = ongoingGameSession,
                learningsMap = learningsMap,
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun newGame(): Job = launch {
        Telemetry.info(tag = TAG, message = "New game")
        if (config.getPreferences().showTutorial) {
            updateState { it.copy(newGameDialog = true) }
        } else navigate(screen = Screen.NewGame)
    }

    private fun newGameWithoutTutorial(): Job = launch {
        config.setPreferences { it.copy(showTutorial = false) }
        navigate(screen = Screen.NewGame)
    }

    private fun newGameWithTutorial(): Job = launch {
        Telemetry.info(tag = TAG, message = "Show tutorial")
        config.setPreferences { it.copy(showTutorial = false) }
        navigate(screen = Screen.Tutorial, stateBuilder = TutorialStateBuilder.NewGame(newGame = true))
    }

    override fun goBack(state: MainMenuState) {
        when (state.currentContent) {
            Content.MAIN_MENU -> {}
            Content.LEARN_MENU -> updateState { it.copy(currentContent = Content.MAIN_MENU) }
            Content.HOST_DEFINITION,
            Content.PLANET_DEFINITION,
            Content.HABITABILITY -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
        }
    }

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.NewGame -> newGame()
            MainMenuAction.HideNewGameDialog -> updateState { it.copy(newGameDialog = false) }
            MainMenuAction.NoNewGameDialog -> newGameWithoutTutorial()
            MainMenuAction.YesNewGameDialog -> newGameWithTutorial()
            MainMenuAction.Next -> navigate(screen = Screen.Game)
            MainMenuAction.Learn -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
            MainMenuAction.Scores -> navigate(screen = Screen.Score)
            MainMenuAction.Achievements -> navigate(screen = Screen.Achievement)
            MainMenuAction.Credits -> navigate(screen = Screen.Credit)
            MainMenuAction.StellarExplorer -> navigate(screen = Screen.StellarExplorer)
            MainMenuAction.HostDefinition -> updateState { it.copy(currentContent = Content.HOST_DEFINITION) }
            MainMenuAction.PlanetDefinition -> updateState { it.copy(currentContent = Content.PLANET_DEFINITION) }
            MainMenuAction.Mechanics -> navigate(screen = Screen.Tutorial)
            MainMenuAction.Habitability -> updateState { it.copy(currentContent = Content.HABITABILITY) }
        }
    }

    companion object {
        private const val TAG = "MainMenuStore"
    }
}
