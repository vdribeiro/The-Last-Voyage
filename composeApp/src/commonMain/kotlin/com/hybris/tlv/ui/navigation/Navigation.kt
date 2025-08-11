package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.Core
import com.hybris.tlv.config.Config
import com.hybris.tlv.flow.launch
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.screen.achievement.AchievementStore
import com.hybris.tlv.ui.screen.credits.CreditsState
import com.hybris.tlv.ui.screen.credits.CreditsStore
import com.hybris.tlv.ui.screen.error.ErrorState
import com.hybris.tlv.ui.screen.error.ErrorStore
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.ui.screen.explore.ExploreState
import com.hybris.tlv.ui.screen.explore.ExploreStore
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class Navigation(private val core: Core) {

    data class State(
        val music: Boolean = true,
        val screen: Screen = Screen.SPLASH,
        val state: Any? = null
    )

    enum class Screen {
        ERROR,
        SPLASH,
        MAIN_MENU,
        NEW_GAME,
        GAME,
        EVENT,
        GAME_OVER,
        EXPLORE,
        STELLAR_EXPLORER,
        SCORES,
        ACHIEVEMENTS,
        CREDITS,
    }

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(
        value = State(music = core.localConfig.getBoolean(key = Config.Music))
    )
    val stateFlow: StateFlow<State> get() = _stateFlow

    fun navigate(screen: Screen, state: Any? = null) {
        core.dispatcher.main.launch { _stateFlow.update { it.copy(screen = screen, state = state) } }
    }

    fun setMusic(enabled: Boolean) {
        core.localConfig.put(key = Config.Music, value = enabled)
        core.dispatcher.main.launch { _stateFlow.update { it.copy(music = enabled) } }
    }

    @Composable
    fun Screen(
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
        Screen.EXPLORE -> ExploreScreen(state = state)
        Screen.STELLAR_EXPLORER -> StellarExplorerScreen(state = state)
        Screen.SCORES -> ScoreScreen(state = state)
        Screen.ACHIEVEMENTS -> AchievementScreen(state = state)
        Screen.CREDITS -> CreditsScreen(state = state)
    }

    @Composable
    private fun ErrorScreen(state: Any?) = com.hybris.tlv.ui.screen.error.ErrorScreen(
        store = ErrorStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? ErrorState ?: ErrorState()
        )
    )

    @Composable
    private fun SplashScreen(state: Any?) = com.hybris.tlv.ui.screen.splash.SplashScreen(
        store = SplashStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? SplashState ?: SplashState(),
            core = core
        )
    )

    @Composable
    private fun MainMenuScreen(state: Any?) = com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen(
        store = MainMenuStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? MainMenuState ?: MainMenuState(),
            remoteConfig = core.remoteConfig,
            gameSessionUseCases = core.useCases.gameSession
        )
    )

    @Composable
    private fun NewGameScreen(state: Any?) = com.hybris.tlv.ui.screen.newgame.NewGameScreen(
        store = NewGameStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? NewGameState ?: NewGameState(),
            earthUseCases = core.useCases.earth,
            gameSessionUseCases = core.useCases.gameSession
        )
    )

    @Composable
    private fun GameScreen(state: Any?) = com.hybris.tlv.ui.screen.game.GameScreen(
        store = GameStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? GameState ?: GameState(),
            spaceUseCases = core.useCases.space,
            exoplanetUseCases = core.useCases.exoplanet,
            gameSessionUseCases = core.useCases.gameSession
        )
    )

    @Composable
    private fun EventScreen(state: Any?) = com.hybris.tlv.ui.screen.event.EventScreen(
        store = EventStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? EventState ?: EventState(),
            eventUseCases = core.useCases.event,
            gameSessionUseCases = core.useCases.gameSession
        )
    )

    @Composable
    private fun GameOverScreen(state: Any?) = com.hybris.tlv.ui.screen.gameover.GameOverScreen(
        store = GameOverStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? GameOverState ?: GameOverState(),
            locale = core.locale,
            gameSessionUseCases = core.useCases.gameSession
        )
    )

    @Composable
    private fun ExploreScreen(state: Any?) = com.hybris.tlv.ui.screen.explore.ExploreScreen(
        store = ExploreStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? ExploreState ?: ExploreState(),
        )
    )

    @Composable
    private fun StellarExplorerScreen(state: Any?) = com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen(
        store = StellarExplorerStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? StellarExplorerState ?: StellarExplorerState(),
            spaceUseCases = core.useCases.space,
            exoplanetUseCases = core.useCases.exoplanet
        )
    )

    @Composable
    private fun ScoreScreen(state: Any?) = com.hybris.tlv.ui.screen.score.ScoreScreen(
        store = ScoreStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? ScoreState ?: ScoreState(),
            locale = core.locale,
            gameSessionUseCases = core.useCases.gameSession
        )
    )

    @Composable
    private fun AchievementScreen(state: Any?) = com.hybris.tlv.ui.screen.achievement.AchievementScreen(
        store = AchievementStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? AchievementState ?: AchievementState(),
            achievementUseCases = core.useCases.achievement
        )
    )

    @Composable
    private fun CreditsScreen(state: Any?) = com.hybris.tlv.ui.screen.credits.CreditsScreen(
        store = CreditsStore(
            dispatcher = core.dispatcher,
            navigation = this,
            initialState = state as? CreditsState ?: CreditsState(),
            creditsUseCases = core.useCases.credits
        )
    )
}
