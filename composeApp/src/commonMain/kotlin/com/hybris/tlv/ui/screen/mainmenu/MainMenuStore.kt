package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.learning.LearningUseCases
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType

internal sealed interface MainMenuAction {
    data object Feedback: MainMenuAction
    data object NewGame: MainMenuAction
    data object Continue: MainMenuAction
    data object Learn: MainMenuAction
    data object Scores: MainMenuAction
    data object Achievements: MainMenuAction
    data object Credits: MainMenuAction
    data object Soon: MainMenuAction
    data object StellarExplorer: MainMenuAction
    data object HostDefinition: MainMenuAction
    data object PlanetDefinition: MainMenuAction
    data object Mechanics: MainMenuAction
    data object Habitability: MainMenuAction
}

internal data class MainMenuState(
    val featureFeedback: Boolean = false,
    val featureSoon: Boolean = false,
    val featureLearn: Boolean = false,
    val featureScores: Boolean = false,
    val featureAchievements: Boolean = false,
    val featureStellarExplorer: Boolean = false,
    val featureNewGame: Boolean = false,
    val loading: Boolean = true,
    val currentContent: Content = Content.MAIN_MENU,
    val ongoingGameSession: Boolean = false,
    val learningsMap: Map<LearningType, List<Learning>> = emptyMap(),
    val developerCorner: String? = null,
    val support: String? = null,
    val formula: String? = null,
)

internal enum class Content {
    MAIN_MENU,
    LEARN_MENU,
    HOST_DEFINITION,
    PLANET_DEFINITION,
    MECHANICS,
    HABITABILITY,
}

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
    init {
        setup()
    }

    private fun setup() = launch {
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        val learnings = learningUseCases.getLearnings().groupBy { it.type }
        val featureFeedback = config.configs.featureFeedback
        val featureSoon = config.configs.featureSoon
        val featureLearn = config.configs.featureLearn
        val featureScores = config.configs.featureScores
        val featureAchievements = config.configs.featureAchievements
        val featureStellarExplorer = config.configs.featureStellarExplorer
        val featureNewGame = config.configs.featureNewGame
        val developerCorner = config.configs.developerCorner
        val support = config.configs.support
        val formula = config.configs.formula

        updateState {
            it.copy(
                featureFeedback = featureFeedback,
                featureSoon = featureSoon,
                featureLearn = featureLearn,
                featureScores = featureScores,
                featureAchievements = featureAchievements,
                featureStellarExplorer = featureStellarExplorer,
                featureNewGame = featureNewGame,
                loading = false,
                ongoingGameSession = ongoingGameSession,
                learningsMap = learnings,
                developerCorner = developerCorner,
                support = support,
                formula = formula,
            )
        }
    }

    override fun setBackNavigation() = {
        when (stateFlow.value.currentContent) {
            Content.MAIN_MENU -> {}
            Content.LEARN_MENU -> updateState { it.copy(currentContent = Content.MAIN_MENU) }

            Content.HOST_DEFINITION,
            Content.PLANET_DEFINITION,
            Content.HABITABILITY,
            Content.MECHANICS -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
        }.let {}
    }

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.Feedback -> navigate(
                screen = Screen.FEEDBACK, state = FeedbackState(
                    screen = Screen.MAIN_MENU,
                    identifier = state.currentContent.name
                )
            )

            MainMenuAction.NewGame -> navigate(screen = Screen.NEW_GAME)
            MainMenuAction.Continue -> navigate(screen = Screen.GAME)

            MainMenuAction.Learn -> updateState {
                it.copy(currentContent = Content.LEARN_MENU)
            }

            MainMenuAction.Scores -> navigate(screen = Screen.SCORE)
            MainMenuAction.Achievements -> navigate(screen = Screen.ACHIEVEMENT)
            MainMenuAction.Credits -> navigate(screen = Screen.CREDIT)
            MainMenuAction.Soon -> {
                // TODO - Soon
            }

            MainMenuAction.StellarExplorer -> navigate(screen = Screen.STELLAR_EXPLORER)

            MainMenuAction.HostDefinition -> updateState {
                it.copy(currentContent = Content.HOST_DEFINITION)
            }

            MainMenuAction.PlanetDefinition -> updateState {
                it.copy(currentContent = Content.PLANET_DEFINITION)
            }

            MainMenuAction.Mechanics -> updateState {
                it.copy(currentContent = Content.MECHANICS)
            }

            MainMenuAction.Habitability -> updateState {
                it.copy(currentContent = Content.HABITABILITY)
            }
        }
    }
}
