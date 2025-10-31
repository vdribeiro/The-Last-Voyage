package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventStateBuilder
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverStateBuilder
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.help.HelpStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameStateBuilder
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStateBuilder
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialStore
import com.hybris.tlv.usecase.UseCases

/**
 * Helper class to create stores.
 */
internal class StoreFactory(
    private val dispatcher: Dispatcher,
    private val navigation: NavigationManager,
    private val audioPlayer: AudioPlayer,
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(): SplashStore = SplashStore(
        dispatcher = dispatcher,
        navigation = navigation,
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
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? MainMenuStateBuilder ?: MainMenuStateBuilder.Default,
        config = config,
        gameSessionUseCases = useCases.gameSession,
    )

    fun createHelpStore(): HelpStore = HelpStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        config = config,
        learningUseCases = useCases.learning
    )

    fun createFeedbackStore(stateBuilder: Any? = null): FeedbackStore = FeedbackStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? FeedbackStateBuilder ?: FeedbackStateBuilder.Feedback
    )

    fun createNewGameStore(stateBuilder: Any? = null): NewGameStore = NewGameStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? NewGameStateBuilder ?: NewGameStateBuilder.Default,
        shipUseCases = useCases.ship,
        catastropheUseCases = useCases.catastrophe,
        gameSessionUseCases = useCases.gameSession
    )

    fun createTutorialStore(stateBuilder: Any? = null): TutorialStore = TutorialStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? TutorialStateBuilder ?: TutorialStateBuilder.NewGame(newGame = false)
    )

    fun createGameStore(stateBuilder: Any? = null): GameStore = GameStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        stateBuilder = stateBuilder as? GameStateBuilder ?: GameStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createEventStore(stateBuilder: Any? = null): EventStore = EventStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        eventUseCases = useCases.event,
        stateBuilder = stateBuilder as? EventStateBuilder ?: EventStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession
    )

    fun createGameOverStore(stateBuilder: Any? = null): GameOverStore = GameOverStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? GameOverStateBuilder ?: GameOverStateBuilder.Default,
        gameSessionUseCases = useCases.gameSession,
        achievementUseCases = useCases.achievement
    )

    fun createStellarExplorerStore(stateBuilder: Any? = null): StellarExplorerStore = StellarExplorerStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder as? StellarExplorerStateBuilder ?: StellarExplorerStateBuilder.Default,
        spaceUseCases = useCases.space
    )

    fun createScoreStore(): ScoreStore = ScoreStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        gameSessionUseCases = useCases.gameSession
    )

    fun createAchievementStore(): AchievementStore = AchievementStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        achievementUseCases = useCases.achievement
    )

    fun createCreditStore(): CreditStore = CreditStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        creditUseCases = useCases.credit
    )
}
