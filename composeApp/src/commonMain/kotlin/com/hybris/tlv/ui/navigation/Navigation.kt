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
import com.hybris.tlv.ui.screen.error.ErrorScreen
import com.hybris.tlv.ui.screen.error.ErrorState
import com.hybris.tlv.ui.screen.error.ErrorStore
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.GameStore
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.screen.gameover.GameOverStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStore
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.newgame.NewGameStore
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.screen.score.ScoreStore
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen
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
    override fun Screen(
        screen: Screen,
        state: Any?
    ) = when (screen) {
        Screen.ERROR -> ErrorScreen(
            store = ErrorStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? ErrorState ?: ErrorState()
            )
        )

        Screen.SPLASH -> SplashScreen(
            store = SplashStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? SplashState ?: SplashState(),
                syncUseCases = useCases.sync
            )
        )

        Screen.MAIN_MENU -> MainMenuScreen(
            store = MainMenuStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? MainMenuState ?: MainMenuState(),
                config = config,
                gameSessionUseCases = useCases.gameSession,
                learningUseCases = useCases.learning
            )
        )

        Screen.NEW_GAME -> NewGameScreen(
            store = NewGameStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? NewGameState ?: NewGameState(),
                earthUseCases = useCases.earth,
                gameSessionUseCases = useCases.gameSession
            )
        )

        Screen.GAME -> GameScreen(
            store = GameStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? GameState ?: GameState(),
                shipUseCases = useCases.ship,
                spaceUseCases = useCases.space,
                gameSessionUseCases = useCases.gameSession
            )
        )

        Screen.EVENT -> EventScreen(
            store = EventStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? EventState ?: EventState(),
                eventUseCases = useCases.event,
                gameSessionUseCases = useCases.gameSession
            )
        )

        Screen.GAME_OVER -> GameOverScreen(
            store = GameOverStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? GameOverState ?: GameOverState(),
                gameSessionUseCases = useCases.gameSession
            )
        )

        Screen.STELLAR_EXPLORER -> StellarExplorerScreen(
            store = StellarExplorerStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? StellarExplorerState ?: StellarExplorerState(),
                spaceUseCases = useCases.space,
            )
        )

        Screen.SCORE -> ScoreScreen(
            store = ScoreStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? ScoreState ?: ScoreState(),
                gameSessionUseCases = useCases.gameSession
            )
        )

        Screen.ACHIEVEMENT -> AchievementScreen(
            store = AchievementStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? AchievementState ?: AchievementState(),
                achievementUseCases = useCases.achievement
            )
        )

        Screen.CREDIT -> CreditScreen(
            store = CreditStore(
                dispatcher = dispatcher,
                navigation = this,
                initialState = state as? CreditState ?: CreditState(),
                creditUseCases = useCases.credit
            )
        )
    }
}
