package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
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
    fun createSplashStore(): SplashStore = SplashStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        config = config,
        useCases = useCases
    )

    fun createMainMenuStore(stateBuilder: Any? = null): MainMenuStore = MainMenuStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        config = config,
        useCases = useCases,
        stateBuilder = stateBuilder
    )

    fun createFeedbackStore(stateBuilder: Any? = null): FeedbackStore = FeedbackStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder
    )

    fun createNewGameStore(stateBuilder: Any? = null): NewGameStore = NewGameStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        useCases = useCases,
        stateBuilder = stateBuilder
    )

    fun createTutorialStore(stateBuilder: Any? = null): TutorialStore = TutorialStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        stateBuilder = stateBuilder
    )

    fun createGameStore(stateBuilder: Any? = null): GameStore = GameStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        useCases = useCases,
        stateBuilder = stateBuilder
    )

    fun createEventStore(stateBuilder: Any? = null): EventStore = EventStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        useCases = useCases,
        stateBuilder = stateBuilder
    )

    fun createGameOverStore(stateBuilder: Any? = null): GameOverStore = GameOverStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        useCases = useCases,
        stateBuilder = stateBuilder
    )

    fun createStellarExplorerStore(stateBuilder: Any? = null): StellarExplorerStore = StellarExplorerStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        useCases = useCases,
        stateBuilder = stateBuilder
    )

    fun createScoreStore(): ScoreStore = ScoreStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        useCases = useCases,
    )

    fun createAchievementStore(): AchievementStore = AchievementStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        useCases = useCases,
    )

    fun createCreditStore(): CreditStore = CreditStore.get(
        dispatcher = dispatcher,
        navigation = navigation,
        audioPlayer = audioPlayer,
        useCases = useCases,
    )
}
