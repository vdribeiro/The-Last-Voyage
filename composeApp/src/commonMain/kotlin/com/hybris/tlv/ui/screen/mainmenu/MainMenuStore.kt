package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.storage.Config
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases

internal sealed interface MainMenuAction {
    data object NewGame: MainMenuAction
    data object Continue: MainMenuAction
    data object Learn: MainMenuAction
    data object Scores: MainMenuAction
    data object Achievements: MainMenuAction
    data object Credits: MainMenuAction
    data object StellarExplorer: MainMenuAction
    data object HostTypes: MainMenuAction
    data object PlanetTypes: MainMenuAction
    data object Properties: MainMenuAction
    data object Mechanics: MainMenuAction
    data object Habitability: MainMenuAction
}

internal data class MainMenuState(
    val currentContent: Content = Content.MAIN_MENU,
    val ongoingGameSession: Boolean = false,
    val developerCorner: String? = null,
    val tip: String? = null,
)

internal enum class Content {
    MAIN_MENU,
    LEARN_MENU,
    HOST_TYPES,
    PLANET_TYPES,
    PROPERTIES,
    MECHANICS,
    HABITABILITY,
}

internal class MainMenuStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: MainMenuState?,
    private val remoteConfig: RemoteConfig,
    private val gameSessionUseCases: GameSessionUseCases
): Store<MainMenuAction, MainMenuState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState ?: MainMenuState()
) {
    init {
        if (initialState == null) setup()
    }

    private fun setup() = launchInPipeline {
        val ongoingGameSession = gameSessionUseCases.isGameSessionOngoing()
        val developerCorner = remoteConfig.getString(key = Config.DeveloperCorner)
        val tip = remoteConfig.getString(key = Config.Tip)
        updateState {
            it.copy(
                ongoingGameSession = ongoingGameSession,
                developerCorner = developerCorner,
                tip = tip,
            )
        }
    }

    override fun setBackNavigation(state: MainMenuState) = {
        when (state.currentContent) {
            Content.MAIN_MENU, Content.LEARN_MENU -> updateState { it.copy(currentContent = Content.MAIN_MENU) }
            Content.HOST_TYPES,
            Content.PLANET_TYPES,
            Content.PROPERTIES,
            Content.MECHANICS,
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

            MainMenuAction.StellarExplorer -> navigate(screen = Screen.STELLAR_EXPLORER)

            MainMenuAction.HostTypes -> updateState {
                it.copy(currentContent = Content.HOST_TYPES)
            }

            MainMenuAction.PlanetTypes -> updateState {
                it.copy(currentContent = Content.PLANET_TYPES)
            }

            MainMenuAction.Properties -> updateState {
                it.copy(currentContent = Content.PROPERTIES)
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
