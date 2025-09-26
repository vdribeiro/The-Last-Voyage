package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.navigation.NavigationManager.State
import com.hybris.tlv.ui.screen.achievement.AchievementScreen
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen
import com.hybris.tlv.ui.screen.tutorial.TutorialScreen
import com.hybris.tlv.ui.store.StoreFactory
import com.hybris.tlv.usecase.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class Navigation(
    private val dispatcher: Dispatcher,
    private val audioPlayer: AudioPlayer?,
    private val config: ConfigManager,
    private val useCases: UseCases,
    state: State = State()
): NavigationManager {

    private val storeFactory: StoreFactory = StoreFactory(
        dispatcher = dispatcher,
        navigation = this,
        audioPlayer = audioPlayer,
        config = config,
        useCases = useCases
    )
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(value = state)
    override val stateFlow: StateFlow<State> get() = _stateFlow

    override var back: () -> Unit = {}

    override fun navigate(screen: Screen, stateBuilder: Any?) {
        dispatcher.main.launch { _stateFlow.update { it.copy(screen = screen, stateBuilder = stateBuilder) } }
    }

    @Composable
    override fun Screen(state: State) {
        with(receiver = config.localConfigs) {
            when (state.screen) {
                Screen.SPLASH -> SplashScreen(store = storeFactory.createSplashStore())
                Screen.MAIN_MENU -> MainMenuScreen(store = storeFactory.createMainMenuStore(stateBuilder = state.stateBuilder))
                Screen.FEEDBACK -> FeedbackScreen(store = storeFactory.createFeedbackStore(stateBuilder = state.stateBuilder))
                Screen.NEW_GAME -> if (featureNewGame) NewGameScreen(store = storeFactory.createNewGameStore()) else Screen(state = State())
                Screen.TUTORIAL -> if (featureTutorial) TutorialScreen(store = storeFactory.createTutorialStore()) else Screen(state = State())
                Screen.GAME -> if (featureGame) GameScreen(store = storeFactory.createGameStore()) else Screen(state = State())
                Screen.EVENT -> if (featureEvents) EventScreen(store = storeFactory.createEventStore()) else Screen(state = State())
                Screen.GAME_OVER -> if (featureGameOver) GameOverScreen(store = storeFactory.createGameOverStore()) else Screen(state = State())
                Screen.STELLAR_EXPLORER -> if (featureStellarExplorer) StellarExplorerScreen(store = storeFactory.createStellarExplorerStore()) else Screen(state = State())
                Screen.SCORE -> if (featureScores) ScoreScreen(store = storeFactory.createScoreStore()) else Screen(state = State())
                Screen.ACHIEVEMENT -> if (featureAchievements) AchievementScreen(store = storeFactory.createAchievementStore()) else Screen(state = State())
                Screen.CREDIT -> CreditScreen(store = storeFactory.createCreditStore())
            }
        }
    }
}
