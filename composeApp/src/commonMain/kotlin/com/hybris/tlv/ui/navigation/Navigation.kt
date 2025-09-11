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

    override fun navigate(screen: Screen, state: Any?) {
        dispatcher.main.launch { _stateFlow.update { it.copy(screen = screen, state = state) } }
    }

    @Composable
    override fun Screen(screen: Screen, state: Any?) {
        with(receiver = config.configs) {
            when (screen) {
                Screen.SPLASH -> SplashScreen(store = storeFactory.createSplashStore(state = state))
                Screen.MAIN_MENU -> MainMenuScreen(store = storeFactory.createMainMenuStore(state = state))
                Screen.FEEDBACK -> if (featureFeedback) FeedbackScreen(store = storeFactory.createFeedbackStore(state = state)) else Screen(screen = Screen.MAIN_MENU)
                Screen.NEW_GAME -> if (featureNewGame) NewGameScreen(store = storeFactory.createNewGameStore(state = state)) else Screen(screen = Screen.GAME)
                Screen.GAME -> if (featureGame) GameScreen(store = storeFactory.createGameStore(state = state)) else Screen(screen = Screen.GAME_OVER)
                Screen.EVENT -> if (featureEvents) EventScreen(store = storeFactory.createEventStore(state = state)) else Screen(screen = Screen.GAME)
                Screen.GAME_OVER -> if (featureGameOver) GameOverScreen(store = storeFactory.createGameOverStore(state = state)) else Screen(screen = Screen.MAIN_MENU)
                Screen.STELLAR_EXPLORER -> if (featureStellarExplorer) StellarExplorerScreen(store = storeFactory.createStellarExplorerStore(state = state)) else Screen(screen = Screen.MAIN_MENU)
                Screen.SCORE -> if (featureScores) ScoreScreen(store = storeFactory.createScoreStore(state = state)) else Screen(screen = Screen.MAIN_MENU)
                Screen.ACHIEVEMENT -> if (featureAchievements) AchievementScreen(store = storeFactory.createAchievementStore(state = state)) else Screen(screen = Screen.MAIN_MENU)
                Screen.CREDIT -> CreditScreen(store = storeFactory.createCreditStore(state = state))
            }
        }
    }
}
