package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.navigation.NavigationManager.State
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.ui.screen.error.ErrorState
import com.hybris.tlv.ui.screen.error.ErrorStore
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStore
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
    val dispatcher: Dispatcher,
    val locale: Locale,
    val remoteConfig: RemoteConfig,
    val useCases: UseCases
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
        Screen.ERROR -> ErrorScreen(state = state)
        Screen.SPLASH -> SplashScreen(state = state)
        Screen.MAIN_MENU -> MainMenuScreen(state = state)
        Screen.NEW_GAME -> NewGameScreen(state = state)
        Screen.GAME -> GameScreen(state = state)
        Screen.EVENT -> EventScreen(state = state)
        Screen.GAME_OVER -> GameOverScreen(state = state)
        Screen.STELLAR_EXPLORER -> StellarExplorerScreen(state = state)
        Screen.SCORE -> ScoreScreen(state = state)
        Screen.ACHIEVEMENT -> AchievementScreen(state = state)
        Screen.CREDIT -> CreditScreen(state = state)
    }

    @Composable
    private fun ErrorScreen(state: Any?) = com.hybris.tlv.ui.screen.error.ErrorScreen(
        store = ErrorStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? ErrorState ?: ErrorState()
        )
    )

    @Composable
    private fun SplashScreen(state: Any?) = com.hybris.tlv.ui.screen.splash.SplashScreen(
        store = SplashStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? SplashState ?: SplashState(),
            syncUseCases = useCases.sync
        )
    )

    @Composable
    private fun MainMenuScreen(state: Any?) = com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen(
        store = MainMenuStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? MainMenuState ?: MainMenuState(),
            remoteConfig = remoteConfig,
            gameSessionUseCases = useCases.gameSession
        )
    )

    @Composable
    private fun NewGameScreen(state: Any?) = com.hybris.tlv.ui.screen.newgame.NewGameScreen(
        store = NewGameStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? NewGameState ?: NewGameState(),
            earthUseCases = useCases.earth,
            gameSessionUseCases = useCases.gameSession
        )
    )

    @Composable
    private fun GameScreen(state: Any?) = com.hybris.tlv.ui.screen.game.GameScreen(
        store = GameStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? GameState ?: GameState(),
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            gameSessionUseCases = useCases.gameSession
        )
    )

    @Composable
    private fun EventScreen(state: Any?) = com.hybris.tlv.ui.screen.event.EventScreen(
        store = EventStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? EventState ?: EventState(),
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession
        )
    )

    @Composable
    private fun GameOverScreen(state: Any?) = com.hybris.tlv.ui.screen.gameover.GameOverScreen(
        store = GameOverStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? GameOverState ?: GameOverState(),
            locale = locale,
            gameSessionUseCases = useCases.gameSession
        )
    )

    @Composable
    private fun StellarExplorerScreen(state: Any?) = com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen(
        store = StellarExplorerStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? StellarExplorerState ?: StellarExplorerState(),
            spaceUseCases = useCases.space,
        )
    )

    @Composable
    private fun ScoreScreen(state: Any?) = com.hybris.tlv.ui.screen.score.ScoreScreen(
        store = ScoreStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? ScoreState ?: ScoreState(),
            locale = locale,
            gameSessionUseCases = useCases.gameSession
        )
    )

    @Composable
    private fun AchievementScreen(state: Any?) = com.hybris.tlv.ui.screen.achievement.AchievementScreen(
        store = AchievementStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? AchievementState ?: AchievementState(),
            achievementUseCases = useCases.achievement
        )
    )

    @Composable
    private fun CreditScreen(state: Any?) = com.hybris.tlv.ui.screen.credit.CreditScreen(
        store = CreditStore(
            dispatcher = dispatcher,
            navigation = this,
            initialState = state as? CreditState ?: CreditState(),
            creditUseCases = useCases.credit
        )
    )
}
