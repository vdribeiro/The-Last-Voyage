package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.ui.screen.achievement.AchievementStateBuilder
import com.hybris.tlv.ui.screen.credit.CreditStateBuilder
import com.hybris.tlv.ui.screen.event.EventStateBuilder
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.screen.gameover.GameOverStateBuilder
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.screen.newgame.NewGameStateBuilder
import com.hybris.tlv.ui.screen.score.ScoreStateBuilder
import com.hybris.tlv.ui.screen.splash.SplashStateBuilder
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerStateBuilder
import com.hybris.tlv.ui.screen.tutorial.TutorialStateBuilder
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigation manager with the screens index.
 */
internal interface NavigationManager {
    /**
     * Current navigation destination.
     */
    data class NavigationState<StateBuilder>(
        val screen: Screen<StateBuilder>,
        val stateBuilder: StateBuilder
    )

    /**
     * The flow of navigation states collected by the App.
     */
    val stateFlow: StateFlow<NavigationState<*>>

    /**
     * A callback for the back action, handled by the App's [androidx.compose.ui.backhandler.BackHandler]
     * and implemented optionally in the current screen's Store.
     */
    var back: () -> Unit

    /**
     * Navigates to a new screen.
     */
    fun <StateBuilder> navigate(screen: Screen<StateBuilder>, state: StateBuilder) {}

    /**
     * The main composable responsible for rendering the current screen based on the navigation state.
     */
    @Composable
    fun Screen(navigationState: NavigationState<*>) {
    }

    /**
     * A sealed interface defining all possible screens in the app.
     * Each screen is a data object that specifies the type of StateBuilder it requires.
     */
    sealed interface Screen<B> {
        data object Splash : Screen<SplashStateBuilder>
        data object MainMenu : Screen<MainMenuStateBuilder>
        data object Feedback : Screen<FeedbackStateBuilder>
        data object NewGame : Screen<NewGameStateBuilder>
        data object Tutorial : Screen<TutorialStateBuilder>
        data object Game : Screen<GameStateBuilder>
        data object Event : Screen<EventStateBuilder>
        data object GameOver : Screen<GameOverStateBuilder>
        data object StellarExplorer : Screen<StellarExplorerStateBuilder>
        data object Score : Screen<ScoreStateBuilder>
        data object Achievement : Screen<AchievementStateBuilder>
        data object Credit : Screen<CreditStateBuilder>
    }
}
