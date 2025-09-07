package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.Core
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.gameSession
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.space.model.TravelOutcome
import database.AppDatabase
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.game.Content as GameContent
import com.hybris.tlv.ui.screen.gameover.Content as GameOverContent
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent
import com.hybris.tlv.ui.screen.newgame.Content as NewGameContent
import com.hybris.tlv.ui.screen.stellarexplorer.Content as StellarExplorerContent

@Composable
private fun Screen(
    screen: Screen,
    state: Any?
) = Core(
    sqlDriver = AndroidSqliteDriver(
        context = LocalContext.current,
        schema = AppDatabase.Schema,
    )
).navigation.Screen(
    screen = screen,
    state = state
)

@Preview
@Composable
private fun ErrorScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.FEEDBACK,
            state = FeedbackState()
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.SPLASH,
            state = SplashState()
        )
    }
}

@Preview
@Composable
private fun MainMenuScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.MAIN_MENU,
            state = MainMenuState(
                currentContent = MainMenuContent.MAIN_MENU,
                ongoingGameSession = false,
                developerCorner = "link",
                support = "link",
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuContinueScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.MAIN_MENU,
            state = MainMenuState(
                currentContent = MainMenuContent.MAIN_MENU,
                ongoingGameSession = true,
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuLearnScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.MAIN_MENU,
            state = MainMenuState(
                currentContent = MainMenuContent.LEARN_MENU,
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuHostsScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.MAIN_MENU,
            state = MainMenuState(
                currentContent = MainMenuContent.HOST_DEFINITION,
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuPlanetsScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.MAIN_MENU,
            state = MainMenuState(
                currentContent = MainMenuContent.PLANET_DEFINITION,
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuHabitabilityScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.MAIN_MENU,
            state = MainMenuState(
                currentContent = MainMenuContent.HABITABILITY,
            )
        )
    }
}

@Preview
@Composable
private fun NewGameShipScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.NEW_GAME,
            state = NewGameState(
                currentContent = NewGameContent.SHIP,
            )
        )
    }
}

@Preview
@Composable
private fun NewGameAdvancedScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.NEW_GAME,
            state = NewGameState(
                currentContent = NewGameContent.ADVANCED
            )
        )
    }
}

@Preview
@Composable
private fun NewGameStartScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.NEW_GAME,
            state = NewGameState(
                currentContent = NewGameContent.START,
                selectedCatastrophe = catastrophes.random()
            )
        )
    }
}

@Preview
@Composable
private fun GameTravelScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.GAME,
            state = GameState(
                gameSession = gameSession,
                currentContent = GameContent.TRAVEL,
                nearStellarHosts = stellarHosts,
            )
        )
    }
}

@Preview
@Composable
private fun GameSystemScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.GAME,
            state = GameState(
                gameSession = gameSession,
                currentContent = GameContent.SYSTEM,
                currentStellarHost = stellarHosts.first().apply {
                    planets.addAll(elements = planets.filter { it.stellarHostId == id })
                    travelOutcome = TravelOutcome(
                        integrity = 5,
                        fuel = 10
                    )
                },
            )
        )
    }
}

@Preview
@Composable
private fun GameShipScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.GAME,
            state = GameState(
                gameSession = gameSession,
                currentContent = GameContent.SHIP,
            )
        )
    }
}

@Preview
@Composable
private fun EventScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.EVENT,
            state = EventState(
                event = events.random()
            )
        )
    }
}

@Preview
@Composable
private fun GameOverMessageScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.GAME_OVER,
            state = GameOverState(
                currentContent = GameOverContent.MESSAGE,
                gameSession = gameSession,
                gameOverMessage = "Game over man! Game over!"
            )
        )
    }
}

@Preview
@Composable
private fun GameOverScoreScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.GAME_OVER,
            state = GameOverState(
                currentContent = GameOverContent.SCORE,
                gameSession = gameSession,
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.STELLAR_EXPLORER,
            state = StellarExplorerState(
                currentContent = StellarExplorerContent.LIST_HOSTS,
                stellarHosts = stellarHosts
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerDetailScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.STELLAR_EXPLORER,
            state = StellarExplorerState(
                currentContent = StellarExplorerContent.DETAIL_HOSTS,
                selectedStellarHost = stellarHosts.first().apply {
                    planets.addAll(elements = planets.filter { it.stellarHostId == id })
                }
            )
        )
    }
}

@Preview
@Composable
private fun ScoreScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.SCORE,
            state = ScoreState(
                scores = listOf(
                    gameSession.copy(id = generateUuid(), score = 100.0),
                    gameSession.copy(id = generateUuid(), score = 50.0),
                    gameSession.copy(id = generateUuid(), score = 150.0),
                    gameSession.copy(id = generateUuid(), score = 1000.0)
                )
            )
        )
    }
}

@Preview
@Composable
private fun AchievementScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.ACHIEVEMENT,
            state = AchievementState(
                achievements = achievements
            )
        )
    }
}

@Preview
@Composable
private fun CreditScreenPreview() {
    AppTheme {
        Screen(
            screen = Screen.ACHIEVEMENT,
            state = CreditState(
                credits = credits
            )
        )
    }
}
