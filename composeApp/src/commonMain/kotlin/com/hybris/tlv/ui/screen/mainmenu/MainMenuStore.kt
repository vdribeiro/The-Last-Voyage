package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.learning.LearningUseCases
import kotlinx.coroutines.Job

internal class MainMenuStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    stateBuilder: MainMenuStateBuilder,
    config: ConfigManager,
    private val gameSessionUseCases: GameSessionUseCases,
    private val learningUseCases: LearningUseCases
): Store<MainMenuAction, MainMenuState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = MainMenuState(
        loading = true,
        featureFeedback = config.configs.featureFeedback,
        featureSoon = config.configs.featureSoon,
        featureLearn = config.configs.featureLearn,
        featureScores = config.configs.featureScores,
        featureAchievements = config.configs.featureAchievements,
        featureStellarExplorer = config.configs.featureStellarExplorer,
        featureNewGame = config.configs.featureNewGame,
        developerCorner = config.configs.developerCorner,
        support = config.configs.support,
        formula = config.configs.formula,
        currentContent = stateBuilder.currentContent ?: Content.MAIN_MENU,
        ongoingGameSession = false,
        learningsMap = emptyMap(),
    )
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        val learningsMap = learningUseCases.getLearnings().groupBy { it.type }

        updateState {
            it.copy(
                loading = false,
                ongoingGameSession = ongoingGameSession,
                learningsMap = learningsMap,
            )
        }
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
            MainMenuAction.Feedback -> navigate(screen = Screen.FEEDBACK)
            MainMenuAction.NewGame -> navigate(screen = Screen.NEW_GAME)
            MainMenuAction.Continue -> navigate(screen = Screen.GAME)
            MainMenuAction.Learn -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
            MainMenuAction.Scores -> navigate(screen = Screen.SCORE)
            MainMenuAction.Achievements -> navigate(screen = Screen.ACHIEVEMENT)
            MainMenuAction.Credits -> navigate(screen = Screen.CREDIT)
            MainMenuAction.Soon -> {} // TODO
            MainMenuAction.StellarExplorer -> navigate(screen = Screen.STELLAR_EXPLORER)
            MainMenuAction.HostDefinition -> updateState { it.copy(currentContent = Content.HOST_DEFINITION) }
            MainMenuAction.PlanetDefinition -> updateState { it.copy(currentContent = Content.PLANET_DEFINITION) }
            MainMenuAction.Mechanics -> {} //TODO: navigate(screen = Screen.GAME, state = GameState(tutorial = Tutorial.YES))
            MainMenuAction.Habitability -> updateState { it.copy(currentContent = Content.HABITABILITY) }
        }
    }
}
