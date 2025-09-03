package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.learning.LearningUseCases
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType

internal sealed interface MainMenuAction {
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
    val loading: Boolean = true,
    val currentContent: Content = Content.MAIN_MENU,
    val ongoingGameSession: Boolean = false,
    val learningsMap: Map<LearningType, List<Learning>> = emptyMap(),
    val developerCorner: String? = null,
    val tip: String? = null,
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
        val developerCorner = config.configs.developerCorner
        val tip = config.configs.tip
        val formula = config.configs.formula
        updateState {
            it.copy(
                loading = false,
                ongoingGameSession = ongoingGameSession,
                learningsMap = learnings,
                developerCorner = developerCorner,
                tip = tip,
                formula = formula,
            )
        }
    }

    override fun setBackNavigation() = {
        when (stateFlow.value.currentContent) {
            Content.MAIN_MENU -> {}
            Content.LEARN_MENU,
            Content.MECHANICS -> updateState { it.copy(currentContent = Content.MAIN_MENU) }

            Content.HOST_DEFINITION,
            Content.PLANET_DEFINITION,
            Content.HABITABILITY -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
        }.let {}
    }

    override fun reducer(state: MainMenuState, action: MainMenuAction) {
        when (action) {
            MainMenuAction.NewGame -> navigate(screen = Screen.NEW_GAME)
            MainMenuAction.Continue -> navigate(screen = Screen.GAME)

            MainMenuAction.Learn -> updateState {
                it.copy(currentContent = Content.LEARN_MENU)
            }

            MainMenuAction.Scores -> navigate(screen = Screen.SCORE)
            MainMenuAction.Achievements -> navigate(screen = Screen.ACHIEVEMENT)
            MainMenuAction.Credits -> navigate(screen = Screen.CREDIT)
            MainMenuAction.Soon -> {
                // TODO
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
