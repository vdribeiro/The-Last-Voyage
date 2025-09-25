package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.ui.screen.tutorial.TutorialAction
import com.hybris.tlv.ui.screen.tutorial.TutorialState
import com.hybris.tlv.ui.screen.tutorial.TutorialStore
import com.hybris.tlv.usecase.UseCases

internal class StoreFactory(
    private val dispatcher: Dispatcher,
    private val navigation: NavigationManager,
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(): SplashStore {
        return SplashStore(
            dispatcher = dispatcher,
            navigation = navigation,
            config = config,
            translateUseCases = useCases.translation,
            archiveUseCases = useCases.archive,
            learningUseCases = useCases.learning,
            catastropheUseCases = useCases.catastrophe,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            eventUseCases = useCases.event,
            achievementUseCases = useCases.achievement,
            creditUseCases = useCases.credit
        )
    }

    fun createMainMenuStore(stateBuilder: Any? = null): MainMenuStore {
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

    fun createFeedbackStore(stateBuilder: Any? = null): FeedbackStore {
        val stateBuilder = stateBuilder as? FeedbackStateBuilder ?: FeedbackStateBuilder()
        return FeedbackStore(
            dispatcher = dispatcher,
            navigation = navigation,
            stateBuilder = stateBuilder
        )
    }

    fun createNewGameStore(): NewGameStore {
        return NewGameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            catastropheUseCases = useCases.catastrophe,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createTutorialStore(): Store<TutorialAction, TutorialState> {
        return TutorialStore(
            dispatcher = dispatcher,
            navigation = navigation
        )
    }

    fun createGameStore(): GameStore {
        return GameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createEventStore(): EventStore {
        return EventStore(
            dispatcher = dispatcher,
            navigation = navigation,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createGameOverStore(): GameOverStore {
        return GameOverStore(
            dispatcher = dispatcher,
            navigation = navigation,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createStellarExplorerStore(): StellarExplorerStore {
        return StellarExplorerStore(
            dispatcher = dispatcher,
            navigation = navigation,
            spaceUseCases = useCases.space
        )
    }

    fun createScoreStore(): ScoreStore {
        return ScoreStore(
            dispatcher = dispatcher,
            navigation = navigation,
            gameSessionUseCases = useCases.gameSession
        )
    }

    fun createAchievementStore(): AchievementStore {
        return AchievementStore(
            dispatcher = dispatcher,
            navigation = navigation,
            achievementUseCases = useCases.achievement
        )
    }

    fun createCreditStore(): CreditStore {
        return CreditStore(
            dispatcher = dispatcher,
            navigation = navigation,
            creditUseCases = useCases.credit
        )
    }
}
