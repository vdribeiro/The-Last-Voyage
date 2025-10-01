package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager.NavigationState
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class Navigation(
    private val dispatcher: Dispatcher,
    private val audioPlayer: AudioPlayer,
    private val config: ConfigManager,
    private val useCases: UseCases,
    initialState: NavigationState = NavigationState()
): NavigationManager {

    private val storeFactory: StoreFactory = StoreFactory(
        dispatcher = dispatcher,
        navigation = this,
        audioPlayer = audioPlayer,
        config = config,
        useCases = useCases
    )
    private val stack: MutableList<NavigationState> = mutableListOf(NavigationState())
    private val _stateFlow: MutableStateFlow<NavigationState> = MutableStateFlow(value = initialState)
    override val stateFlow: StateFlow<NavigationState> get() = _stateFlow

    override var back: () -> Unit = {}

    override fun goBack(): Job = dispatcher.main.launch {
        if (stack.size > 1) {
            stack.removeLast()
            _stateFlow.update { stack.last() }
        }
    }

    override fun navigate(screen: Screen, stateBuilder: Any?, savableState: Any?): Job = dispatcher.main.launch {
        if (stack.isNotEmpty()) stack[stack.lastIndex] = stack.last().copy(stateBuilder = savableState)
        val navigationState = NavigationState(screen = screen, stateBuilder = stateBuilder)
        val index = stack.indexOf(element = navigationState)
        if (index != -1) stack.subList(index, stack.size).clear()
        stack.add(element = navigationState)
        _stateFlow.value = navigationState
    }

    private fun fallback(): Job = navigate(screen = Screen.Splash)

    @Composable
    override fun Screen(navigationState: NavigationState) {
        with(receiver = config.localConfigs) {
            when (navigationState.screen) {
                Screen.Splash -> SplashScreen(store = storeFactory.createSplashStore())
                Screen.MainMenu -> MainMenuScreen(store = storeFactory.createMainMenuStore(stateBuilder = navigationState.stateBuilder))
                Screen.Feedback -> FeedbackScreen(store = storeFactory.createFeedbackStore(stateBuilder = navigationState.stateBuilder))
                Screen.NewGame -> if (featureNewGame) NewGameScreen(store = storeFactory.createNewGameStore(stateBuilder = navigationState.stateBuilder)) else fallback()
                Screen.Tutorial -> if (featureTutorial) TutorialScreen(store = storeFactory.createTutorialStore(stateBuilder = navigationState.stateBuilder)) else fallback()
                Screen.Game -> if (featureGame) GameScreen(store = storeFactory.createGameStore(stateBuilder = navigationState.stateBuilder)) else fallback()
                Screen.Event -> if (featureEvents) EventScreen(store = storeFactory.createEventStore(stateBuilder = navigationState.stateBuilder)) else fallback()
                Screen.GameOver -> if (featureGameOver) GameOverScreen(store = storeFactory.createGameOverStore(stateBuilder = navigationState.stateBuilder)) else fallback()
                Screen.StellarExplorer -> if (featureStellarExplorer) StellarExplorerScreen(store = storeFactory.createStellarExplorerStore(stateBuilder = navigationState.stateBuilder)) else fallback()
                Screen.Score -> if (featureScores) ScoreScreen(store = storeFactory.createScoreStore()) else fallback()
                Screen.Achievement -> if (featureAchievements) AchievementScreen(store = storeFactory.createAchievementStore()) else fallback()
                Screen.Credit -> CreditScreen(store = storeFactory.createCreditStore())
            }
        }
    }
}
