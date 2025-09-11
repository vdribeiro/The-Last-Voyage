package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.navigation.NavigationManager.State
import com.hybris.tlv.ui.screen.achievement.AchievementScreen
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
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
                Screen.SPLASH -> Splash(state = state)
                Screen.MAIN_MENU -> MainMenu(state = state)
                Screen.FEEDBACK -> Feedback(state = state)
                Screen.NEW_GAME -> NewGame(state = state)
                Screen.GAME -> Game(state = state)
                Screen.EVENT -> Event(state = state)
                Screen.GAME_OVER -> GameOver(state = state)
                Screen.STELLAR_EXPLORER -> StellarExplorer(state = state)
                Screen.SCORE -> Score(state = state)
                Screen.ACHIEVEMENT -> Achievement(state = state)
                Screen.CREDIT -> Credit(state = state)
            }
        }
    }

    @Composable
    private fun Splash(state: Any? = null) = SplashScreen(
        store = storeFactory.createSplashStore(state = state)
    )

    @Composable
    private fun MainMenu(state: Any? = null) = MainMenuScreen(
        store = storeFactory.createMainMenuStore(state = state)
    )

    @Composable
    private fun Feedback(state: Any? = null) = with(receiver = config.configs) {
        if (featureFeedback) FeedbackScreen(
            store = storeFactory.createFeedbackStore(state = state)
        ) else MainMenu()
    }

    @Composable
    private fun NewGame(state: Any? = null) = with(receiver = config.configs) {
        if (featureNewGame) NewGameScreen(
            store = NewGameStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? NewGameState ?: NewGameState(),
                catastropheUseCases = useCases.catastrophe,
                gameSessionUseCases = useCases.gameSession
            )
        ) else Game()
    }

    @Composable
    private fun Game(state: Any? = null) = with(receiver = config.configs) {
        if (featureGame) GameScreen(
            store = GameStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? GameState ?: GameState(),
                shipUseCases = useCases.ship,
                spaceUseCases = useCases.space,
                gameSessionUseCases = useCases.gameSession
            )
        ) else GameOver()
    }

    @Composable
    private fun Event(state: Any? = null) = with(receiver = config.configs) {
        if (featureEvents) EventScreen(
            store = EventStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? EventState ?: EventState(),
                eventUseCases = useCases.event,
                gameSessionUseCases = useCases.gameSession
            )
        ) else Game()
    }

    @Composable
    private fun GameOver(state: Any? = null) = with(receiver = config.configs) {
        if (featureGameOver) GameOverScreen(
            store = GameOverStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? GameOverState ?: GameOverState(),
                gameSessionUseCases = useCases.gameSession
            )
        ) else MainMenu()
    }

    @Composable
    private fun StellarExplorer(state: Any? = null) = with(receiver = config.configs) {
        if (featureStellarExplorer) StellarExplorerScreen(
            store = StellarExplorerStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? StellarExplorerState ?: StellarExplorerState(),
                spaceUseCases = useCases.space,
            )
        ) else MainMenu()
    }

    @Composable
    private fun Score(state: Any? = null) = with(receiver = config.configs) {
        if (featureScores) ScoreScreen(
            store = ScoreStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? ScoreState ?: ScoreState(),
                gameSessionUseCases = useCases.gameSession
            )
        ) else MainMenu()
    }

    @Composable
    private fun Achievement(state: Any? = null) = with(receiver = config.configs) {
        if (featureAchievements) AchievementScreen(
            store = AchievementStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? AchievementState ?: AchievementState(),
                achievementUseCases = useCases.achievement
            )
        ) else MainMenu()
    }

    @Composable
    private fun Credit(state: Any? = null) = CreditScreen(
        store = CreditStore(
            dispatcher = dispatcher,
            navigation = this@Navigation,
            initialState = state as? CreditState ?: CreditState(),
            creditUseCases = useCases.credit
        )
    )
}
