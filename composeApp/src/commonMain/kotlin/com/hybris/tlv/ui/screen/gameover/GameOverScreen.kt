package com.hybris.tlv.ui.screen.gameover

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.core.locale.getLocalDateTime
import com.hybris.tlv.domain.ship.Engine
import com.hybris.tlv.domain.ship.Ship
import com.hybris.tlv.domain.usecase.gamesession.model.GameOver
import com.hybris.tlv.domain.usecase.gamesession.model.GameSession
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.bottombar.Snackbar
import com.hybris.tlv.ui.theme.component.card.ScoreCard
import com.hybris.tlv.ui.theme.component.container.TypewriterContent
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun GameOverScreen(store: Store<GameOverState, GameOverAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val gameSession = storeState.gameSession
    val ship = gameSession?.ship
    val achievement = storeState.achievement

    val gameOverTranslation = getTranslation(key = "game_over_screen__game_over")
    val messageTranslation = getTranslation(key = "game_over_screen__score")
    val scoreTranslation = getTranslation(key = "game_over_screen__end")
    val newAchievementTranslation = getTranslation(key = "achievements_screen__new")

    Screen(
        loading = storeState.loading,
        onBackClick = null,
        bottomBar = {
            if (storeState.loading) return@Screen
            val text = when (storeState.currentContent) {
                Content.MESSAGE -> messageTranslation
                Content.SCORE -> scoreTranslation
            }
            ButtonsBar(
                buttons = persistentListOf(text),
                id = { it },
                text = { getTranslation(key = it) },
                onClick = { store.send(action = GameOverAction.Next) }
            )
        },
        snackbarHost = {
            if (achievement != null) {
                val message = "$newAchievementTranslation: ${getTranslation(key = achievement.id)}"
                Snackbar(
                    message = message,
                    onDismiss = { store.send(action = GameOverAction.NextAchievement) }
                )
            }
        }
    ) {
        when (storeState.currentContent) {
            Content.MESSAGE -> TypewriterContent(
                modifier = Modifier
                    .testTag(tag = "game_over_message_content")
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = gameOverTranslation,
                text = storeState.gameOver?.displayName?.let { getTranslation(key = it) },
            )

            Content.SCORE -> TypewriterContent(
                modifier = Modifier
                    .testTag(tag = "game_over_score_content")
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = gameOverTranslation
            ) {
                if (gameSession != null && ship != null) {
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    ScoreCard(
                        modifier = Modifier.testTag(tag = "game_over_score"),
                        score = gameSession.score,
                        utc = gameSession.utc,
                        settledPlanet = gameSession.settledPlanetName,
                        habitability = gameSession.finalHabitability,
                        assignedPoints = gameSession.ship.assignedPoints,
                        yearsTraveled = ship.yearsTraveled,
                        sensorRange = ship.sensorRange,
                        integrity = ship.integrity,
                        materials = ship.materials,
                        fuel = ship.fuel,
                        cryopods = ship.cryopods,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameOverScreenLoadingPreview() = Preview {
    GameOverScreen(
        store = Store(
            initialState = GameOverState(
                loading = true,
                currentContent = Content.MESSAGE,
                gameSession = null,
                gameOver = null
            )
        )
    )
}

@Preview
@Composable
private fun GameOverScreenMessagePreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "game_over_screen__game_over",
                value = "Game Over"
            ),
            PreviewTranslation(
                key = "game_over_screen__default_game_over",
                value = "The game is over, but do not worry, you can always play again and try different outcomes. " +
                        "Just make sure you take care of yourself."
            ),
            PreviewTranslation(
                key = "game_over_screen__score",
                value = "Score"
            ),
        )
    )
    GameOverScreen(
        store = Store(
            initialState = GameOverState(
                loading = false,
                currentContent = Content.MESSAGE,
                gameSession = null,
                gameOver = GameOver.GAME_OVER
            )
        )
    )
}

@Preview
@Composable
private fun GameOverScreenScorePreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "game_over_screen__game_over",
                value = "Game Over"
            ),
            PreviewTranslation(
                key = "game_over_screen__end",
                value = "End"
            ),
            PreviewTranslation(
                key = "settled_planet",
                value = "Planet"
            ),
            PreviewTranslation(
                key = "final_habitability",
                value = "Habitability"
            ),
            PreviewTranslation(
                key = "engine",
                value = "Engine"
            ),
            PreviewTranslation(
                key = "points",
                value = "Points"
            ),
            PreviewTranslation(
                key = "ship_years_traveled",
                value = "Years"
            ),
            PreviewTranslation(
                key = "ship_sensor",
                value = "Sensor Range"
            ),
            PreviewTranslation(
                key = "ship_integrity",
                value = "Integrity"
            ),
            PreviewTranslation(
                key = "ship_materials",
                value = "Materials"
            ),
            PreviewTranslation(
                key = "ship_fuel",
                value = "Fuel"
            ),
            PreviewTranslation(
                key = "ship_cryopods",
                value = "Cryopods"
            ),
        )
    )
    GameOverScreen(
        store = Store(
            initialState = GameOverState(
                loading = false,
                currentContent = Content.SCORE,
                gameSession = GameSession(
                    id = "2",
                    utc = getLocalDateTime(),
                    ship = Ship(
                        id = "1",
                        engine = Engine(
                            id = "1",
                            description = "",
                            velocity = 0.1,
                            fuelConsumption = 0.0,
                            cost = 0
                        ),
                        assignedPoints = 10,
                        yearsTraveled = 100.0,
                        sensorRange = 5,
                        integrity = 80,
                        fuel = 100,
                        materials = 90,
                        cryopods = 150,
                    ),
                    currentStellarHostId = null,
                    visitedStellarHosts = emptySet(),
                    launchedEvents = emptySet(),
                    settledPlanetId = "earth",
                    settledPlanetName = "earth",
                    finalHabitability = 90.0,
                    score = 9000.0,
                    formula = Formula(id = "1")
                ),
                gameOver = GameOver.GAME_OVER
            )
        )
    )
}
