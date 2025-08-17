package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.datetime.now
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
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
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.space.formula.Constants.HABITABLE_ZONE_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_ECCENTRICITY_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_ESI_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_MASS_IDEAL_UPPER_LIMIT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_MASS_LOWER_LIMIT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_MASS_MAX_UPPER_LIMIT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_MASS_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_OBLIQUITY_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_PROTECTION_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_RADIUS_IDEAL_UPPER_LIMIT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_RADIUS_LOWER_LIMIT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_RADIUS_MAX_UPPER_LIMIT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_RADIUS_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_TELLURICITY_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_TEMPERATURE_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.PLANET_TIDAL_LOCKING_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.ROCHE_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_ACTIVITY_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_AGE_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_GRAVITY_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_HOST_EFFECTIVE_TEMPERATURE_MAX_DEVIATION
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_MASS_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_METALLICITY_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_ROTATIONAL_PERIOD_WEIGHT
import com.hybris.tlv.usecase.space.formula.Constants.STELLAR_SPECTRAL_TYPE_WEIGHT
import com.hybris.tlv.usecase.space.model.TravelOutcome
import database.AppDatabase
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.explore.Content as ExploreContent
import com.hybris.tlv.ui.screen.game.Content as GameContent
import com.hybris.tlv.ui.screen.gameover.Content as GameOverContent
import com.hybris.tlv.ui.screen.newgame.Content as NewGameContent
import com.hybris.tlv.ui.screen.stellarexplorer.Content as StellarExplorerContent

private val mock = Mock()

@Composable
private fun Screen(
    screen: Screen,
    state: Any?
) = Mock(
    databaseDriver = AndroidSqliteDriver(
        context = LocalContext.current,
        schema = AppDatabase.Schema
    )
).Screen(
    screen = screen,
    state = state
)

private val gameSession = GameSession(
    id = "1",
    utc = now(),
    assignedPoints = 10,
    yearsTraveled = 100.0,
    sensorRange = 5,
    integrity = 80,
    fuel = 100,
    materials = 90,
    cryopods = 150,
    currentStellarHostId = mock.stellarHosts.first().id,
    visitedStellarHosts = emptySet(),
    launchedEvents = emptySet(),
    settledPlanetId = null,
    finalHabitability = null,
    score = null,
    rocheWeight = ROCHE_WEIGHT,
    habitableZoneWeight = HABITABLE_ZONE_WEIGHT,
    planetRadiusWeight = PLANET_RADIUS_WEIGHT,
    planetMassWeight = PLANET_MASS_WEIGHT,
    planetTelluricityWeight = PLANET_TELLURICITY_WEIGHT,
    planetEccentricityWeight = PLANET_ECCENTRICITY_WEIGHT,
    planetTemperatureWeight = PLANET_TEMPERATURE_WEIGHT,
    planetObliquityWeight = PLANET_OBLIQUITY_WEIGHT,
    planetEsiWeight = PLANET_ESI_WEIGHT,
    stellarSpectralTypeWeight = STELLAR_SPECTRAL_TYPE_WEIGHT,
    stellarMassWeight = STELLAR_MASS_WEIGHT,
    stellarAgeWeight = STELLAR_AGE_WEIGHT,
    stellarActivityWeight = STELLAR_ACTIVITY_WEIGHT,
    stellarRotationalPeriodWeight = STELLAR_ROTATIONAL_PERIOD_WEIGHT,
    stellarGravityWeight = STELLAR_GRAVITY_WEIGHT,
    stellarMetallicityWeight = STELLAR_METALLICITY_WEIGHT,
    stellarEffectiveTemperatureWeight = STELLAR_EFFECTIVE_TEMPERATURE_WEIGHT,
    planetProtectionWeight = PLANET_PROTECTION_WEIGHT,
    planetTidalLockingWeight = PLANET_TIDAL_LOCKING_WEIGHT,
    planetMassLowerLimit = PLANET_MASS_LOWER_LIMIT,
    planetMassIdealUpperLimit = PLANET_MASS_IDEAL_UPPER_LIMIT,
    planetMassMaxUpperLimit = PLANET_MASS_MAX_UPPER_LIMIT,
    planetRadiusLowerLimit = PLANET_RADIUS_LOWER_LIMIT,
    planetRadiusIdealUpperLimit = PLANET_RADIUS_IDEAL_UPPER_LIMIT,
    planetRadiusMaxUpperLimit = PLANET_RADIUS_MAX_UPPER_LIMIT,
    stellarHostEffectiveTemperatureMaxDeviation = STELLAR_HOST_EFFECTIVE_TEMPERATURE_MAX_DEVIATION
)

@Preview
@Composable
private fun ErrorScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.ERROR,
            state = ErrorState()
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.SPLASH,
            state = SplashState()
        )
    }
}

@Preview
@Composable
private fun MainMenuScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.MAIN_MENU,
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
        mock.Screen(
            screen = Screen.MAIN_MENU,
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
        mock.Screen(
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
        mock.Screen(
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
        mock.Screen(
            screen = Screen.NEW_GAME,
            state = NewGameState(
                currentContent = NewGameContent.START,
                selectedCatastrophe = mock.catastrophes.random()
            )
        )
    }
}

@Preview
@Composable
private fun GameTravelScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.GAME,
            state = GameState(
                gameSession = gameSession,
                currentContent = GameContent.TRAVEL,
                nearStellarHosts = mock.stellarHosts,
            )
        )
    }
}

@Preview
@Composable
private fun GameSystemScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.GAME,
            state = GameState(
                gameSession = gameSession,
                currentContent = GameContent.SYSTEM,
                stellarHosts = mock.stellarHosts,
                currentStellarHost = mock.stellarHosts.first().apply {
                    planets.addAll(elements = mock.planets.filter { it.stellarHostId == id })
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
        mock.Screen(
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
        mock.Screen(
            screen = Screen.EVENT,
            state = EventState(
                event = mock.events.random()
            )
        )
    }
}

@Preview
@Composable
private fun GameOverMessageScreenPreview() {
    AppTheme {
        mock.Screen(
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
        mock.Screen(
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
private fun ExploreScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.EXPLORE,
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
        mock.Screen(
            screen = Screen.EXPLORE,
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
        mock.Screen(
            screen = Screen.EXPLORE,
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
        mock.Screen(
            screen = Screen.STELLAR_EXPLORER,
            state = StellarExplorerState(
                currentContent = StellarExplorerContent.LIST_HOSTS,
                stellarHosts = mock.stellarHosts
            )
        )
    }
}

@Preview
@Composable
private fun StellarExplorerDetailScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.STELLAR_EXPLORER,
            state = StellarExplorerState(
                currentContent = StellarExplorerContent.DETAIL_HOSTS,
                selectedStellarHost = mock.stellarHosts.first().apply {
                    planets.addAll(elements = mock.planets.filter { it.stellarHostId == id })
                }
            )
        )
    }
}

@Preview
@Composable
private fun ScoreScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.SCORES,
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
        mock.Screen(
            screen = Screen.ACHIEVEMENTS,
            state = AchievementState(
                achievements = mock.achievements
            )
        )
    }
}

@Preview
@Composable
private fun CreditsScreenPreview() {
    AppTheme {
        mock.Screen(
            screen = Screen.ACHIEVEMENTS,
            state = CreditsState(
                credits = mock.credits
            )
        )
    }
}
