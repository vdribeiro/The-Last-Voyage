package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.navigation.NavigationManager.State
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.feedback.FeedbackStore
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStore
import com.hybris.tlv.usecase.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class Navigation(
    private val dispatcher: Dispatcher,
    private val config: ConfigManager,
    private val useCases: UseCases
): NavigationManager {

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
                Screen.SPLASH -> SplashScreen(state = state)
                Screen.MAIN_MENU -> MainMenuScreen(state = state)
                Screen.FEEDBACK -> FeedbackScreen(state = state)
                Screen.NEW_GAME -> NewGameScreen(state = state)
                Screen.GAME -> GameScreen(state = state)
                Screen.EVENT -> EventScreen(state = state)
                Screen.GAME_OVER -> GameOverScreen(state = state)
                Screen.STELLAR_EXPLORER -> StellarExplorerScreen(state = state)
                Screen.SCORE -> ScoreScreen(state = state)
                Screen.ACHIEVEMENT -> AchievementScreen(state = state)
                Screen.CREDIT -> CreditScreen(state = state)
            }
        }
    }

    @Composable
    private fun SplashScreen(state: Any? = null) = com.hybris.tlv.ui.screen.splash.SplashScreen(
        store = SplashStore(
            dispatcher = dispatcher,
            navigation = this@Navigation,
            initialState = state as? SplashState ?: SplashState(),
            syncUseCases = useCases.sync
        )
    )

    @Composable
    private fun MainMenuScreen(state: Any? = null) = com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen(
        store = MainMenuStore(
            dispatcher = dispatcher,
            navigation = this@Navigation,
            initialState = state as? MainMenuState ?: MainMenuState(),
            config = config,
            gameSessionUseCases = useCases.gameSession,
            learningUseCases = useCases.learning
        )
    )

    @Composable
    private fun FeedbackScreen(state: Any? = null) = with(receiver = config.configs) {
        if (featureFeedback) com.hybris.tlv.ui.screen.feedback.FeedbackScreen(
            store = FeedbackStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? FeedbackState ?: FeedbackState()
            )
        ) else MainMenuScreen()
    }

    @Composable
    private fun NewGameScreen(state: Any? = null) = with(receiver = config.configs) {
        if (featureNewGame) com.hybris.tlv.ui.screen.newgame.NewGameScreen(
            store = NewGameStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? NewGameState ?: NewGameState(),
                earthUseCases = useCases.earth,
                gameSessionUseCases = useCases.gameSession
            )
        ) else GameScreen()
    }

    @Composable
    private fun GameScreen(state: Any? = null) = with(receiver = config.configs) {
        if (featureGame) com.hybris.tlv.ui.screen.game.GameScreen(
            store = GameStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? GameState ?: GameState(),
                shipUseCases = useCases.ship,
                spaceUseCases = useCases.space,
                gameSessionUseCases = useCases.gameSession
            )
        ) else GameOverScreen()
    }

    @Composable
    private fun EventScreen(state: Any? = null) = with(receiver = config.configs) {
        if (featureEvents) com.hybris.tlv.ui.screen.event.EventScreen(
            store = EventStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? EventState ?: EventState(),
                eventUseCases = useCases.event,
                gameSessionUseCases = useCases.gameSession
            )
        ) else GameScreen()
    }

    @Composable
    private fun GameOverScreen(state: Any? = null) = with(receiver = config.configs) {
        if (featureGameOver) com.hybris.tlv.ui.screen.gameover.GameOverScreen(
            store = GameOverStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? GameOverState ?: GameOverState(),
                gameSessionUseCases = useCases.gameSession
            )
        ) else MainMenuScreen()
    }

    @Composable
    private fun StellarExplorerScreen(state: Any? = null) = with(receiver = config.configs) {
        if (featureStellarExplorer) com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen(
            store = StellarExplorerStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? StellarExplorerState ?: StellarExplorerState(),
                spaceUseCases = useCases.space,
            )
        ) else MainMenuScreen()
    }

    @Composable
    private fun ScoreScreen(state: Any? = null) = with(receiver = config.configs) {
        if (featureScores) com.hybris.tlv.ui.screen.score.ScoreScreen(
            store = ScoreStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? ScoreState ?: ScoreState(),
                gameSessionUseCases = useCases.gameSession
            )
        ) else MainMenuScreen()
    }

    @Composable
    private fun AchievementScreen(state: Any? = null) = with(receiver = config.configs) {
        if (featureAchievements) com.hybris.tlv.ui.screen.achievement.AchievementScreen(
            store = AchievementStore(
                dispatcher = dispatcher,
                navigation = this@Navigation,
                initialState = state as? AchievementState ?: AchievementState(),
                achievementUseCases = useCases.achievement
            )
        ) else MainMenuScreen()
    }

    @Composable
    private fun CreditScreen(state: Any? = null) = com.hybris.tlv.ui.screen.credit.CreditScreen(
        store = CreditStore(
            dispatcher = dispatcher,
            navigation = this@Navigation,
            initialState = state as? CreditState ?: CreditState(),
            creditUseCases = useCases.credit
        )
    )
}
