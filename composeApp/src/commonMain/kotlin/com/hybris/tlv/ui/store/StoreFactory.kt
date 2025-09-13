package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.usecase.UseCases

internal class StoreFactory(
    private val dispatcher: Dispatcher,
    private val navigation: NavigationManager,
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(): SplashStore =
        SplashStore(
            dispatcher = dispatcher,
            navigation = navigation,
            syncUseCases = useCases.sync
        )

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

    fun createNewGameStore(): NewGameStore =
        NewGameStore(
            dispatcher = dispatcher,
            navigation = navigation,
            catastropheUseCases = useCases.catastrophe,
            gameSessionUseCases = useCases.gameSession
        )

    fun createGameStore(stateBuilder: Any? = null): GameStore {
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

    fun createEventStore(): EventStore =
        EventStore(
            dispatcher = dispatcher,
            navigation = navigation,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession
        )

    fun createGameOverStore(): GameOverStore =
        GameOverStore(
            dispatcher = dispatcher,
            navigation = navigation,
            gameSessionUseCases = useCases.gameSession
        )

    fun createStellarExplorerStore(): StellarExplorerStore =
        StellarExplorerStore(
            dispatcher = dispatcher,
            navigation = navigation,
            spaceUseCases = useCases.space
        )

    fun createScoreStore(): ScoreStore =
        ScoreStore(
            dispatcher = dispatcher,
            navigation = navigation,
            gameSessionUseCases = useCases.gameSession
        )

    fun createAchievementStore(): AchievementStore =
        AchievementStore(
            dispatcher = dispatcher,
            navigation = navigation,
            achievementUseCases = useCases.achievement
        )

    fun createCreditStore(): CreditStore =
        CreditStore(
            dispatcher = dispatcher,
            navigation = navigation,
            creditUseCases = useCases.credit
        )
}
