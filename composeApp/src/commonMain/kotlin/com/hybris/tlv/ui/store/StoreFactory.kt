package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
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
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameStateBuilder
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStateBuilder
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStateBuilder
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStateBuilder
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
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
    fun createSplashStore(stateBuilder: SplashStateBuilder): SplashStore = SplashStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder,
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

    fun createMainMenuStore(stateBuilder: MainMenuStateBuilder): MainMenuStore = MainMenuStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder,
        config = config,
        gameSessionUseCases = useCases.gameSession,
        learningUseCases = useCases.learning
    )

    fun createFeedbackStore(stateBuilder: FeedbackStateBuilder): FeedbackStore = FeedbackStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder
    )

    fun createNewGameStore(stateBuilder: NewGameStateBuilder): NewGameStore = NewGameStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder,
        catastropheUseCases = useCases.catastrophe,
        gameSessionUseCases = useCases.gameSession
    )

    fun createTutorialStore(stateBuilder: TutorialStateBuilder): TutorialStore = TutorialStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder
    )

    fun createGameStore(stateBuilder: GameStateBuilder): GameStore = GameStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        shipUseCases = useCases.ship,
        spaceUseCases = useCases.space,
        stateBuilder = stateBuilder,
        gameSessionUseCases = useCases.gameSession
    )

    fun createEventStore(stateBuilder: EventStateBuilder): EventStore = EventStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        eventUseCases = useCases.event,
        stateBuilder = stateBuilder,
        gameSessionUseCases = useCases.gameSession
    )

    fun createGameOverStore(stateBuilder: GameOverStateBuilder): GameOverStore = GameOverStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder,
        gameSessionUseCases = useCases.gameSession
    )

    fun createStellarExplorerStore(stateBuilder: StellarExplorerStateBuilder): StellarExplorerStore = StellarExplorerStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder,
        spaceUseCases = useCases.space
    )

    fun createScoreStore(stateBuilder: ScoreStateBuilder): ScoreStore = ScoreStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder,
        gameSessionUseCases = useCases.gameSession
    )

    fun createAchievementStore(stateBuilder: AchievementStateBuilder): AchievementStore = AchievementStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder,
        achievementUseCases = useCases.achievement
    )

    fun createCreditStore(stateBuilder: CreditStateBuilder): CreditStore = CreditStore(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder,
        creditUseCases = useCases.credit
    )
}
