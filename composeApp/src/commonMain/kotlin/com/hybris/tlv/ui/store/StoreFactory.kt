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
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackAction
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverAction
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
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
    fun createSplashStore(): Store<SplashAction, SplashState> {
        return SplashStore(
            dispatcher = dispatcher,
            navigation = navigation,
            syncUseCases = useCases.sync
        )
    }

    fun createMainMenuStore(stateBuilder: Any? = null): Store<MainMenuAction, MainMenuState> {
        val stateBuilder = stateBuilder as? MainMenuStateBuilder ?: MainMenuStateBuilder()
        return MainMenuStore(
            dispatcher = dispatcher,
            navigation = navigation,
            stateBuilder = stateBuilder,
            config = config,
            gameSessionUseCases = useCases.gameSession,
            learningUseCases = useCases.learning
        )
    }

    fun createFeedbackStore(stateBuilder: Any? = null): Store<FeedbackAction, FeedbackState> {
        val stateBuilder = stateBuilder as? FeedbackStateBuilder ?: FeedbackStateBuilder()
        return FeedbackStore(
            dispatcher = dispatcher,
            navigation = navigation,
            stateBuilder = stateBuilder
        )
    }

    fun createNewGameStore(): Store<NewGameAction, NewGameState> {
        return NewGameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            catastropheUseCases = useCases.catastrophe,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createGameStore(stateBuilder: Any? = null): Store<GameAction, GameState> {
        val stateBuilder = stateBuilder as? GameStateBuilder ?: GameStateBuilder()
        return GameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            stateBuilder = stateBuilder,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createEventStore(): Store<EventAction, EventState> {
        return EventStore(
            dispatcher = dispatcher,
            navigation = navigation,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createGameOverStore(): Store<GameOverAction, GameOverState> {
        return GameOverStore(
            dispatcher = dispatcher,
            navigation = navigation,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createStellarExplorerStore(): Store<StellarExplorerAction, StellarExplorerState> {
        return StellarExplorerStore(
            dispatcher = dispatcher,
            navigation = navigation,
            spaceUseCases = useCases.space
        )
    }

    fun createScoreStore(): Store<ScoreAction, ScoreState> {
        return ScoreStore(
            dispatcher = dispatcher,
            navigation = navigation,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createAchievementStore(): Store<AchievementAction, AchievementState> {
        return AchievementStore(
            dispatcher = dispatcher,
            navigation = navigation,
            achievementUseCases = useCases.achievement
        )
    }

    fun createCreditStore(): Store<CreditAction, CreditState> {
        return CreditStore(
            dispatcher = dispatcher,
            navigation = navigation,
            creditUseCases = useCases.credit
        )
    }
}