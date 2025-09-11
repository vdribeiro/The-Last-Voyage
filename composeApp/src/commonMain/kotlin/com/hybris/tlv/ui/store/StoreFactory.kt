package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.event.EventAction
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.createEventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackAction
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.feedback.createFeedbackStore
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.createGameStore
import com.hybris.tlv.ui.screen.gameover.GameOverAction
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.screen.gameover.createGameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.createMainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameAction
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.newgame.createNewGameStore
import com.hybris.tlv.ui.screen.splash.SplashAction
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.screen.splash.createSplashStore
import com.hybris.tlv.usecase.UseCases

internal class StoreFactory(
    private val dispatcher: Dispatcher,
    private val navigation: NavigationManager,
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(state: Any? = null): Store<SplashAction, SplashState> {
        val state = state as? SplashState ?: SplashState()
        return createSplashStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            syncUseCases = useCases.sync
        ).apply { setup(state = state) }
    }

    fun createMainMenuStore(state: Any? = null): Store<MainMenuAction, MainMenuState> {
        val state = state as? MainMenuState ?: MainMenuState()
        return createMainMenuStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            config = config,
            gameSessionUseCases = useCases.gameSession,
            learningUseCases = useCases.learning
        ).apply { setup(state = state) }
    }

    fun createFeedbackStore(state: Any? = null): Store<FeedbackAction, FeedbackState> {
        val state = state as? FeedbackState ?: FeedbackState()
        return createFeedbackStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state
        ).apply { setup(state = state) }
    }

    fun createNewGameStore(state: Any? = null): Store<NewGameAction, NewGameState> {
        val state = state as? NewGameState ?: NewGameState()
        return createNewGameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            catastropheUseCases = useCases.catastrophe,
            gameSessionUseCases = useCases.gameSession
        ).apply { setup(state = state) }
    }

     fun createGameStore(state: Any? = null): Store<GameAction, GameState> {
        val state = state as? GameState ?: GameState()
        return createGameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        ).apply { setup(state = state) }
    }

     fun createEventStore(state: Any? = null): Store<EventAction, EventState> {
        val state = state as? EventState ?: EventState()
        return createEventStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession
        ).apply { setup(state = state) }
    }

    fun createGameOverStore(state: Any? = null): Store<GameOverAction, GameOverState> {
        val state = state as? GameOverState ?: GameOverState()
        return createGameOverStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            gameSessionUseCases = useCases.gameSession
        ).apply { setup(state = state) }
    }
}