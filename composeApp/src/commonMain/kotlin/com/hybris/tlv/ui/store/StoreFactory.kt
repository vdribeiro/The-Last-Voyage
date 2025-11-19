package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.screen.achievement.AchievementStateBuilder
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditStateBuilder
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventStateBuilder
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverStateBuilder
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.help.HelpStateBuilder
import com.hybris.tlv.ui.screen.help.HelpStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameStateBuilder
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStateBuilder
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStateBuilder
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialStore
import com.hybris.tlv.usecase.UseCases

/**
 * Store creator.
 */
internal class StoreFactory(
    private val audioPlayer: AudioPlayer,
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(): SplashStore = SplashStore(
        audioPlayer = audioPlayer,
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
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? MainMenuStateBuilder ?: MainMenuStateBuilder.Default,
        config = config,
        gameSessionUseCases = useCases.gameSession,
    )

    fun createHelpStore(stateBuilder: Any? = null): HelpStore = HelpStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? HelpStateBuilder ?: HelpStateBuilder.Default,
        config = config,
        learningUseCases = useCases.learning
    )

    fun createFeedbackStore(stateBuilder: Any? = null): FeedbackStore = FeedbackStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? FeedbackStateBuilder ?: FeedbackStateBuilder.Default
    )

    fun createNewGameStore(stateBuilder: Any? = null): NewGameStore = NewGameStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? NewGameStateBuilder ?: NewGameStateBuilder.Default,
        shipUseCases = useCases.ship,
        catastropheUseCases = useCases.catastrophe,
        gameSessionUseCases = useCases.gameSession
    )

    fun createTutorialStore(stateBuilder: Any? = null): TutorialStore = TutorialStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? TutorialStateBuilder ?: TutorialStateBuilder.Default(newGame = false)
    )

    fun createGameStore(stateBuilder: Any? = null): GameStore = GameStore(
        audioPlayer = audioPlayer,
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        stateBuilder = stateBuilder as? GameStateBuilder ?: GameStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createEventStore(stateBuilder: Any? = null): EventStore = EventStore(
        audioPlayer = audioPlayer,
        eventUseCases = useCases.event,
        stateBuilder = stateBuilder as? EventStateBuilder ?: EventStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createGameOverStore(stateBuilder: Any? = null): GameOverStore = GameOverStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? GameOverStateBuilder ?: GameOverStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession,
        achievementUseCases = useCases.achievement
    )

    fun createStellarExplorerStore(stateBuilder: Any? = null): StellarExplorerStore = StellarExplorerStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? StellarExplorerStateBuilder ?: StellarExplorerStateBuilder.Default,
        spaceUseCases = useCases.space
    )

    fun createScoreStore(stateBuilder: Any? = null): ScoreStore = ScoreStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? ScoreStateBuilder ?: ScoreStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createAchievementStore(stateBuilder: Any? = null): AchievementStore = AchievementStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? AchievementStateBuilder ?: AchievementStateBuilder.Default,
        achievementUseCases = useCases.achievement
    )

    fun createCreditStore(stateBuilder: Any? = null): CreditStore = CreditStore(
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? CreditStateBuilder ?: CreditStateBuilder.Default,
        creditUseCases = useCases.credit
    )
}
