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
        when (navigationState.screen) {
            Screen.Splash -> SplashScreen(store = storeFactory.createSplashStore())
            Screen.MainMenu -> MainMenuScreen(store = storeFactory.createMainMenuStore(stateBuilder = navigationState.stateBuilder))
            Screen.Help -> HelpScreen(store = storeFactory.createHelpStore(stateBuilder = navigationState.stateBuilder))
            Screen.Feedback -> FeedbackScreen(store = storeFactory.createFeedbackStore(stateBuilder = navigationState.stateBuilder))
            Screen.NewGame -> NewGameScreen(store = storeFactory.createNewGameStore(stateBuilder = navigationState.stateBuilder))
            Screen.Tutorial -> TutorialScreen(store = storeFactory.createTutorialStore(stateBuilder = navigationState.stateBuilder))
            Screen.Game -> GameScreen(store = storeFactory.createGameStore(stateBuilder = navigationState.stateBuilder))
            Screen.Event -> EventScreen(store = storeFactory.createEventStore(stateBuilder = navigationState.stateBuilder))
            Screen.GameOver -> GameOverScreen(store = storeFactory.createGameOverStore(stateBuilder = navigationState.stateBuilder))
            Screen.StellarExplorer -> StellarExplorerScreen(store = storeFactory.createStellarExplorerStore(stateBuilder = navigationState.stateBuilder))
            Screen.Score -> ScoreScreen(store = storeFactory.createScoreStore(stateBuilder = navigationState.stateBuilder))
            Screen.Achievement -> AchievementScreen(store = storeFactory.createAchievementStore(stateBuilder = navigationState.stateBuilder))
            Screen.Credit -> CreditScreen(store = storeFactory.createCreditStore(stateBuilder = navigationState.stateBuilder))
        }
    }
}
