package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
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
import com.hybris.tlv.ui.store.StoreFactory
import com.hybris.tlv.usecase.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class Navigation(
    private val dispatcher: Dispatcher,
    private val config: ConfigManager,
    private val useCases: UseCases
): NavigationManager {

    private val storeFactory: StoreFactory = StoreFactory(
        dispatcher = dispatcher,
        navigation = this,
        config = config,
        useCases = useCases
    )
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(value = State())
    override val stateFlow: StateFlow<State> get() = _stateFlow

    override var back: () -> Unit = {}

    override fun navigate(screen: Screen, stateBuilder: Any?) {
        dispatcher.main.launch { _stateFlow.update { it.copy(screen = screen, stateBuilder = stateBuilder) } }
    }

    @Composable
    override fun Screen(screen: Screen, stateBuilder: Any?) {
        with(receiver = config.configs) {
            when (screen) {
                Screen.SPLASH -> SplashScreen(store = storeFactory.createSplashStore(stateBuilder = stateBuilder))
                Screen.MAIN_MENU -> MainMenuScreen(store = storeFactory.createMainMenuStore(stateBuilder = stateBuilder))
                Screen.FEEDBACK -> if (featureFeedback) FeedbackScreen(store = storeFactory.createFeedbackStore(stateBuilder = stateBuilder)) else Screen(screen = Screen.MAIN_MENU)
                Screen.NEW_GAME -> if (featureNewGame) NewGameScreen(store = storeFactory.createNewGameStore(stateBuilder = stateBuilder)) else Screen(screen = Screen.GAME)
                Screen.GAME -> if (featureGame) GameScreen(store = storeFactory.createGameStore(stateBuilder = stateBuilder)) else Screen(screen = Screen.GAME_OVER)
                Screen.EVENT -> if (featureEvents) EventScreen(store = storeFactory.createEventStore(stateBuilder = stateBuilder)) else Screen(screen = Screen.GAME)
                Screen.GAME_OVER -> if (featureGameOver) GameOverScreen(store = storeFactory.createGameOverStore(stateBuilder = stateBuilder)) else Screen(screen = Screen.MAIN_MENU)
                Screen.STELLAR_EXPLORER -> if (featureStellarExplorer) StellarExplorerScreen(store = storeFactory.createStellarExplorerStore(stateBuilder = stateBuilder)) else Screen(screen = Screen.MAIN_MENU)
                Screen.SCORE -> if (featureScores) ScoreScreen(store = storeFactory.createScoreStore(stateBuilder = stateBuilder)) else Screen(screen = Screen.MAIN_MENU)
                Screen.ACHIEVEMENT -> if (featureAchievements) AchievementScreen(store = storeFactory.createAchievementStore(stateBuilder = stateBuilder)) else Screen(screen = Screen.MAIN_MENU)
                Screen.CREDIT -> CreditScreen(store = storeFactory.createCreditStore(stateBuilder = stateBuilder))
            }
        }
    }
}
