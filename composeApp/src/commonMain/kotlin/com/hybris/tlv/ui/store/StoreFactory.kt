package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.achievement.AchievementAction
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditAction
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventAction
import com.hybris.tlv.ui.screen.event.EventStateBuilder
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackAction
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverAction
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameAction
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreAction
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashAction
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerAction
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.usecase.UseCases

internal class StoreFactory(
    private val dispatcher: Dispatcher,
    private val navigation: NavigationManager,
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(state: Any? = null): Store<SplashAction, SplashState> {
        val state = state as? SplashState ?: SplashState()
        return SplashStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            syncUseCases = useCases.sync
        ).apply { setup(state = state) }
    }

    fun createMainMenuStore(state: Any? = null): Store<MainMenuAction, MainMenuState> {
        val state = state as? MainMenuState ?: MainMenuState()
        return MainMenuStore(
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
        return FeedbackStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state
        ).apply { setup(state = state) }
    }

    fun createNewGameStore(state: Any? = null): Store<NewGameAction, NewGameState> {
        val state = state as? NewGameState ?: NewGameState()
        return NewGameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            catastropheUseCases = useCases.catastrophe,
            gameSessionUseCases = useCases.gameSession
        ).apply { setup(state = state) }
    }

    fun createGameStore(state: Any? = null): Store<GameAction, GameState> {
        val state = state as? GameState ?: GameState()
        return GameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        ).apply { setup(state = state) }
    }

    fun createEventStore(stateBuilder: Any? = null): Store<EventAction, EventState> {
        val stateBuilder = stateBuilder as? EventStateBuilder ?: EventStateBuilder()
        return EventStore(
            dispatcher = dispatcher,
            navigation = navigation,
            stateBuilder = stateBuilder,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createGameOverStore(state: Any? = null): Store<GameOverAction, GameOverState> {
        val state = state as? GameOverState ?: GameOverState()
        return GameOverStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            gameSessionUseCases = useCases.gameSession
        ).apply { setup(state = state) }
    }

    fun createStellarExplorerStore(state: Any? = null): Store<StellarExplorerAction, StellarExplorerState> {
        val state = state as? StellarExplorerState ?: StellarExplorerState()
        return StellarExplorerStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            spaceUseCases = useCases.space
        ).apply { setup(state = state) }
    }

    fun createScoreStore(state: Any? = null): Store<ScoreAction, ScoreState> {
        val state = state as? ScoreState ?: ScoreState()
        return ScoreStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            gameSessionUseCases = useCases.gameSession
        ).apply { setup(state = state) }
    }

    fun createAchievementStore(state: Any? = null): Store<AchievementAction, AchievementState> {
        val state = state as? AchievementState ?: AchievementState()
        return AchievementStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            achievementUseCases = useCases.achievement
        ).apply { setup(state = state) }
    }

    fun createCreditStore(state: Any? = null): Store<CreditAction, CreditState> {
        val state = state as? CreditState ?: CreditState()
        return CreditStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            creditUseCases = useCases.credit
        ).apply { setup(state = state) }
    }
}