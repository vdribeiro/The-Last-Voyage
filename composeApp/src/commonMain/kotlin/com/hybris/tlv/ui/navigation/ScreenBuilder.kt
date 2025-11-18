package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import com.hybris.tlv.ui.screen.achievement.AchievementScreen
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.help.HelpScreen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuScreen
import com.hybris.tlv.ui.screen.newgame.NewGameScreen
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerScreen
import com.hybris.tlv.ui.screen.tutorial.TutorialScreen
import com.hybris.tlv.ui.store.StoreFactory

internal class ScreenBuilder(private val storeFactory: StoreFactory) {
    /**
     * The main composable responsible for rendering the current screen based on the navigation state.
     */
    @Composable
    fun Screen(navigationState: NavigationState) {
        when (navigationState.route) {
            Route.Splash -> SplashScreen(store = storeFactory.createSplashStore())
            Route.MainMenu -> MainMenuScreen(store = storeFactory.createMainMenuStore(stateBuilder = navigationState.stateBuilder))
            Route.Help -> HelpScreen(store = storeFactory.createHelpStore(stateBuilder = navigationState.stateBuilder))
            Route.Feedback -> FeedbackScreen(store = storeFactory.createFeedbackStore(stateBuilder = navigationState.stateBuilder))
            Route.NewGame -> NewGameScreen(store = storeFactory.createNewGameStore(stateBuilder = navigationState.stateBuilder))
            Route.Tutorial -> TutorialScreen(store = storeFactory.createTutorialStore(stateBuilder = navigationState.stateBuilder))
            Route.Game -> GameScreen(store = storeFactory.createGameStore(stateBuilder = navigationState.stateBuilder))
            Route.Event -> EventScreen(store = storeFactory.createEventStore(stateBuilder = navigationState.stateBuilder))
            Route.GameOver -> GameOverScreen(store = storeFactory.createGameOverStore(stateBuilder = navigationState.stateBuilder))
            Route.StellarExplorer -> StellarExplorerScreen(store = storeFactory.createStellarExplorerStore(stateBuilder = navigationState.stateBuilder))
            Route.Score -> ScoreScreen(store = storeFactory.createScoreStore(stateBuilder = navigationState.stateBuilder))
            Route.Achievement -> AchievementScreen(store = storeFactory.createAchievementStore(stateBuilder = navigationState.stateBuilder))
            Route.Credit -> CreditScreen(store = storeFactory.createCreditStore(stateBuilder = navigationState.stateBuilder))
        }
    }
}
