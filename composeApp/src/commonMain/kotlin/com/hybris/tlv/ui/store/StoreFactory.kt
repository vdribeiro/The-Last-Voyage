package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.help.HelpStateBuilder
import com.hybris.tlv.ui.screen.help.HelpStore
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.usecase.UseCases

/**
 * Store creator.
 */
internal class StoreFactory(
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(): SplashStore = SplashStore(
        config = config,
        archiveUseCases = useCases.archive,
        translateUseCases = useCases.translation,
        learningUseCases = useCases.learning,
        catastropheUseCases = useCases.catastrophe,
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        eventUseCases = useCases.event,
        achievementUseCases = useCases.achievement,
        creditUseCases = useCases.credit
    )

    fun createMainMenuStore(stateBuilder: Any? = null): MainMenuStore = MainMenuStore(
        stateBuilder = stateBuilder as? MainMenuStateBuilder ?: MainMenuStateBuilder.Default,
        config = config,
        gameSessionUseCases = useCases.gameSession,
    )

    fun createHelpStore(stateBuilder: Any? = null): HelpStore = HelpStore(
        stateBuilder = stateBuilder as? HelpStateBuilder ?: HelpStateBuilder.Default,
        config = config,
        learningUseCases = useCases.learning
    )

    fun createFeedbackStore(stateBuilder: Any? = null): FeedbackStore = FeedbackStore(
        stateBuilder = stateBuilder as? FeedbackStateBuilder ?: FeedbackStateBuilder.Default
    )

    fun createNewGameStore(stateBuilder: Any? = null): NewGameStore = NewGameStore(
        stateBuilder = stateBuilder as? NewGameStateBuilder ?: NewGameStateBuilder.Default,
        shipUseCases = useCases.ship,
        catastropheUseCases = useCases.catastrophe,
        gameSessionUseCases = useCases.gameSession
    )

    fun createTutorialStore(stateBuilder: Any? = null): TutorialStore = TutorialStore(
        stateBuilder = stateBuilder as? TutorialStateBuilder ?: TutorialStateBuilder.Default(newGame = false)
    )

    fun createGameStore(stateBuilder: Any? = null): GameStore = GameStore(
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        stateBuilder = stateBuilder as? GameStateBuilder ?: GameStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createEventStore(stateBuilder: Any? = null): EventStore = EventStore(
        eventUseCases = useCases.event,
        stateBuilder = stateBuilder as? EventStateBuilder ?: EventStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createGameOverStore(stateBuilder: Any? = null): GameOverStore = GameOverStore(
        stateBuilder = stateBuilder as? GameOverStateBuilder ?: GameOverStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession,
        achievementUseCases = useCases.achievement
    )

    fun createStellarExplorerStore(stateBuilder: Any? = null): StellarExplorerStore = StellarExplorerStore(
        stateBuilder = stateBuilder as? StellarExplorerStateBuilder ?: StellarExplorerStateBuilder.Default,
        spaceUseCases = useCases.space
    )

    fun createScoreStore(stateBuilder: Any? = null): ScoreStore = ScoreStore(
        stateBuilder = stateBuilder as? ScoreStateBuilder ?: ScoreStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createAchievementStore(stateBuilder: Any? = null): AchievementStore = AchievementStore(
        stateBuilder = stateBuilder as? AchievementStateBuilder ?: AchievementStateBuilder.Default,
        achievementUseCases = useCases.achievement
    )

    fun createCreditStore(stateBuilder: Any? = null): CreditStore = CreditStore(
        stateBuilder = stateBuilder as? CreditStateBuilder ?: CreditStateBuilder.Default,
        creditUseCases = useCases.credit
    )
}
