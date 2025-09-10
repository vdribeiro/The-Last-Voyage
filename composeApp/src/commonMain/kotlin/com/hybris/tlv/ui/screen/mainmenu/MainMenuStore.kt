package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.Tutorial
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.learning.LearningUseCases
import kotlinx.coroutines.Job

internal class MainMenuStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: MainMenuState,
    private val config: ConfigManager,
    private val gameSessionUseCases: GameSessionUseCases,
    private val learningUseCases: LearningUseCases
): Store<MainMenuAction, MainMenuState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setup(state: MainMenuState): Job = launch {
        val config = config.configs
        val featureFeedback = config.featureFeedback
        val featureSoon = config.featureSoon
        val featureLearn = config.featureLearn
        val featureScores = config.featureScores
        val featureAchievements = config.featureAchievements
        val featureStellarExplorer = config.featureStellarExplorer
        val featureNewGame = config.featureNewGame
        val loading = state.loading
        val currentContent = state.currentContent
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        val learningsMap = learningUseCases.getLearnings().groupBy { it.type }
        val developerCorner = config.developerCorner
        val support = config.support
        val formula = config.formula

        updateState {
            it.copy(
                featureFeedback = featureFeedback,
                featureSoon = featureSoon,
                featureLearn = featureLearn,
                featureScores = featureScores,
                featureAchievements = featureAchievements,
                featureStellarExplorer = featureStellarExplorer,
                featureNewGame = featureNewGame,
                loading = loading,
                currentContent = currentContent,
                ongoingGameSession = ongoingGameSession,
                learningsMap = learningsMap,
                developerCorner = developerCorner,
                support = support,
                formula = formula,
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
            MainMenuAction.Feedback -> navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.MAIN_MENU,
                    tag = state.currentContent.name
                )
            )

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
            MainMenuAction.Mechanics -> navigate(screen = Screen.GAME, state = GameState(tutorial = Tutorial.YES))
            MainMenuAction.Habitability -> updateState { it.copy(currentContent = Content.HABITABILITY) }
        }
    }
}
