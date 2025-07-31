package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.mock.MockCore
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.gameSession
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.screen.achievement.AchievementState
import com.hybris.tlv.ui.screen.credits.CreditsState
import com.hybris.tlv.ui.screen.error.ErrorState
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.explore.ExploreState
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
import com.hybris.tlv.ui.screen.explore.Content as ExploreContent
import com.hybris.tlv.ui.screen.game.Content as GameContent
import com.hybris.tlv.ui.screen.gameover.Content as GameOverContent
import com.hybris.tlv.ui.screen.newgame.Content as NewGameContent
import com.hybris.tlv.ui.screen.stellarexplorer.Content as StellarExplorerContent

@Composable
private fun navigation() =
    MockCore(
        driver = AndroidSqliteDriver(
            context = LocalContext.current,
            schema = AppDatabase.Schema
        )
    ).navigation

@Preview
@Composable
private fun ErrorScreenPreview() {
    AppTheme {
        navigation().ErrorScreen(
            state = ErrorState()
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    AppTheme {
        navigation().SplashScreen(
            state = SplashState()
        )
    }
}

@Preview
@Composable
private fun MainMenuScreenPreview() {
    AppTheme {
        navigation().MainMenuScreen(
            state = MainMenuState(
                ongoingGameSession = false
            )
        )
    }
}

@Preview
@Composable
private fun MainMenuContinueScreenPreview() {
    AppTheme {
        navigation().MainMenuScreen(
            state = MainMenuState(
                ongoingGameSession = true
            )
        )
    }
}

@Preview
@Composable
private fun NewGameShipScreenPreview() {
    AppTheme {
        navigation().NewGameScreen(
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
        navigation().NewGameScreen(
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
        navigation().NewGameScreen(
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
        navigation().GameScreen(
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
        navigation().GameScreen(
            state = GameState(
                gameSession = gameSession,
                currentContent = GameContent.SYSTEM,
                stellarHosts = stellarHosts,
                currentStellarHost = stellarHosts.first().apply {
                    planets.addAll(planets.filter { it.stellarHostId == id })
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
        navigation().GameScreen(
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
        navigation().EventScreen(
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
        navigation().GameOverScreen(
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
        navigation().GameOverScreen(
            state = GameOverState(
                currentContent = GameOverContent.SCORE,
                gameSession = gameSession,
            )
        )
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    AppTheme {
        navigation().ExploreScreen(
            state = ExploreState(
                currentContent = ExploreContent.MENU,
            )
        )
    }
}

@Preview
@Composable
private fun ExploreMechanicsScreenPreview() {
    AppTheme {
        navigation().ExploreScreen(
            state = ExploreState(
                currentContent = ExploreContent.MECHANICS,
            )
        )
    }
}

@Preview
@Composable
private fun ExploreHabitabilityScreenPreview() {
    AppTheme {
        navigation().ExploreScreen(
            state = ExploreState(
                currentContent = ExploreContent.HABITABILITY,
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerScreenPreview() {
    AppTheme {
        navigation().StellarExplorerScreen(
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
        navigation().StellarExplorerScreen(
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
        navigation().ScoreScreen(
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
        navigation().AchievementScreen(
            state = AchievementState(
                achievements = achievements
            )
        )
    }
}

@Preview
@Composable
private fun CreditsScreenPreview() {
    AppTheme {
        navigation().CreditsScreen(
            state = CreditsState(
                credits = credits
            )
        )
    }
}
