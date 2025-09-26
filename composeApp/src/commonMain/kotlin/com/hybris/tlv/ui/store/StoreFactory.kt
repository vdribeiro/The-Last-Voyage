package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.ui.screen.tutorial.TutorialState
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialStore
import com.hybris.tlv.usecase.UseCases

internal class StoreFactory(
    private val dispatcher: Dispatcher,
    private val navigation: NavigationManager,
    private val audioPlayer: AudioPlayer,
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(state: Any? = null): SplashStore = SplashStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        config = config,
        state = state as? SplashState,
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

    fun createMainMenuStore(state: Any? = null): MainMenuStore = MainMenuStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        config = config,
        state = state as? MainMenuState,
        stateBuilder = state as? MainMenuStateBuilder ?: MainMenuStateBuilder(),
        gameSessionUseCases = useCases.gameSession,
        learningUseCases = useCases.learning
    )

    fun createFeedbackStore(state: Any? = null): FeedbackStore = FeedbackStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        state = state as? FeedbackState,
        stateBuilder = state as? FeedbackStateBuilder ?: FeedbackStateBuilder()
    )

    fun createNewGameStore(state: Any? = null): NewGameStore = NewGameStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        state = state as? NewGameState,
        catastropheUseCases = useCases.catastrophe,
        gameSessionUseCases = useCases.gameSession
    )

    fun createTutorialStore(state: Any? = null): TutorialStore = TutorialStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        state = state as? TutorialState,
        stateBuilder = state as? TutorialStateBuilder ?: TutorialStateBuilder()
    )

    fun createGameStore(state: Any? = null): GameStore = GameStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        state = state as? GameState,
        gameSessionUseCases = useCases.gameSession
    )

    fun createEventStore(state: Any? = null): EventStore = EventStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        eventUseCases = useCases.event,
        state = state as? EventState,
        gameSessionUseCases = useCases.gameSession
    )

    fun createGameOverStore(state: Any? = null): GameOverStore = GameOverStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        state = state as? GameOverState,
        gameSessionUseCases = useCases.gameSession
    )

    fun createStellarExplorerStore(state: Any? = null): StellarExplorerStore = StellarExplorerStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        state = state as? StellarExplorerState,
        spaceUseCases = useCases.space
    )

    fun createScoreStore(state: Any? = null): ScoreStore = ScoreStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        state = state as? ScoreState,
        gameSessionUseCases = useCases.gameSession
    )

    fun createAchievementStore(state: Any? = null): AchievementStore = AchievementStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        state = state as? AchievementState,
        achievementUseCases = useCases.achievement
    )

    fun createCreditStore(state: Any? = null): CreditStore = CreditStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        state = state as? CreditState,
        creditUseCases = useCases.credit
    )
}
