package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.config.Preferences
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
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
        MainMenuStateBuilder.Load -> MainMenuState()
        is MainMenuStateBuilder.FromState -> stateBuilder.state
        is MainMenuStateBuilder.FromContent -> MainMenuState(currentContent = stateBuilder.content)
    }
) {
    init {
        when (stateBuilder) {
            MainMenuStateBuilder.Load -> setup()
            is MainMenuStateBuilder.FromState -> {}
            is MainMenuStateBuilder.FromContent -> setup()
        }
    }

    private fun setup(): Job = launch {
        config.fetch()
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        val learningsMap = learningUseCases.getLearnings().groupBy { it.type }

        updateState {
            it.copy(
                loading = false,
                featureSoon = config.localConfigs.featureSoon,
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
    }

    private fun newGame(): Job = launch {
        if (Preferences.get().showTutorial) updateState { it.copy(newGameDialog = true) } else navigate(screen = Screen.NEW_GAME)
    }

    private fun newGameWithoutTutorial(): Job = launch {
        Preferences.set(preferences = Preferences.get().copy(showTutorial = false))
        navigate(screen = Screen.NEW_GAME)
    }

    private fun newGameWithTutorial(): Job = launch {
        Preferences.set(preferences = Preferences.get().copy(showTutorial = false))
        navigate(screen = Screen.TUTORIAL, stateBuilder = TutorialStateBuilder.NewGame(newGame = true))
    }

    override fun back(state: MainMenuState) = {
        when (state.currentContent) {
            Content.MAIN_MENU -> {}
            Content.LEARN_MENU -> updateState { it.copy(currentContent = Content.MAIN_MENU) }
            Content.HOST_DEFINITION,
            Content.PLANET_DEFINITION,
            Content.HABITABILITY -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
        }.let {}
    }

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.NewGame -> newGame()
            MainMenuAction.HideNewGameDialog -> updateState { it.copy(newGameDialog = false) }
            MainMenuAction.NoNewGameDialog -> newGameWithoutTutorial()
            MainMenuAction.YesNewGameDialog -> newGameWithTutorial()
            MainMenuAction.Continue -> navigate(screen = Screen.GAME)
            MainMenuAction.Learn -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
            MainMenuAction.Scores -> navigate(screen = Screen.SCORE)
            MainMenuAction.Achievements -> navigate(screen = Screen.ACHIEVEMENT)
            MainMenuAction.Credits -> navigate(screen = Screen.CREDIT)
            MainMenuAction.Soon -> {} // TODO
            MainMenuAction.StellarExplorer -> navigate(screen = Screen.STELLAR_EXPLORER)
            MainMenuAction.HostDefinition -> updateState { it.copy(currentContent = Content.HOST_DEFINITION) }
            MainMenuAction.PlanetDefinition -> updateState { it.copy(currentContent = Content.PLANET_DEFINITION) }
            MainMenuAction.Mechanics -> navigate(screen = Screen.TUTORIAL)
            MainMenuAction.Habitability -> updateState { it.copy(currentContent = Content.HABITABILITY) }
        }
    }
}
