package com.hybris.tlv.ui.screen.gameover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.locale.now
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.Score
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.ui.theme.component.TypewriterText
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun GameOverScreen(store: Store<GameOverState, GameOverAction>) {
    val storeState by store.stateFlow.collectAsState()
    val gameSession = storeState.gameSession
    val ship = gameSession?.ship
    val gameOverTranslation = remember { getTranslation(key = "game_over_screen__game_over") }
    val messageTranslation = remember { getTranslation(key = "game_over_screen__score") }
    val scoreTranslation = remember { getTranslation(key = "game_over_screen__end") }

    val typography = LocalTypography.current

    Screen(
        modifier = Modifier.testTag(tag = GAME_OVER_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            // Continue button
            Button(
                modifier = Modifier
                    .testTag(tag = GAME_OVER_SCREEN_BUTTON)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                onClick = { store.send(action = GameOverAction.Continue) }
            ) {
                Text(
                    text = when (storeState.currentContent) {
                        Content.MESSAGE -> messageTranslation
                        Content.SCORE -> scoreTranslation
                    }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .testTag(tag = GAME_OVER_SCREEN_CONTENT)
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.testTag(tag = GAME_OVER_SCREEN_TITLE),
                text = gameOverTranslation,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            when (storeState.currentContent) {
                // Game over message
                Content.MESSAGE -> TypewriterText(
                    modifier = Modifier
                        .testTag(tag = GAME_OVER_SCREEN_MESSAGE)
                        .weight(weight = 1f)
                        .fillMaxWidth(),
                    text = getTranslation(key = storeState.gameOver?.displayName.orEmpty())
                )

                // Score
                Content.SCORE -> if (gameSession != null && ship != null) Score(
                    modifier = Modifier.testTag(tag = GAME_OVER_SCREEN_SCORE),
                    isExpanded = null,
                    score = (gameSession.score?.roundTo(decimalPlaces = 2) ?: 0.0).toString(),
                    utc = gameSession.utc,
                    yearsTraveled = ship.yearsTraveled.roundTo(decimalPlaces = 2).toString(),
                    sensorRange = ship.sensorRange.toString(),
                    integrity = ship.integrity.toString(),
                    materials = ship.materials.toString(),
                    fuel = ship.fuel.toString(),
                    cryopods = ship.cryopods.toString()
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameOverLoading() {
    AppTheme {
        GameOverScreen(
            store = getStore(
                initialState = GameOverState(
                    loading = true,
                    currentContent = Content.MESSAGE,
                    gameSession = null,
                    gameOver = null
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameOverMessage() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "game_over_screen__game_over",
                value = "Game Over"
            ),
            Translation(
                key = "game_over_screen__default_game_over",
                value = "The game is over, but do not worry, you can always play again and try different outcomes. " +
                        "Just make sure you take care of yourself."
            ),
            Translation(
                key = "game_over_screen__score",
                value = "Score"
            ),
        )
    )
    AppTheme {
        GameOverScreen(
            store = getStore(
                initialState = GameOverState(
                    loading = false,
                    currentContent = Content.MESSAGE,
                    gameSession = null,
                    gameOver = GameOver.GAME_OVER
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameOverScore() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "game_over_screen__game_over",
                value = "Game Over"
            ),
            Translation(
                key = "game_over_screen__end",
                value = "End"
            ),
        )
    )
    AppTheme {
        GameOverScreen(
            store = getStore(
                initialState = GameOverState(
                    loading = false,
                    currentContent = Content.SCORE,
                    gameSession = GameSession(
                        id = "2",
                        utc = now(),
                        ship = Ship(
                            id = "1",
                            engine = Engine(
                                id = "1",
                                description = "",
                                velocity = 0.1
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
                        finalHabitability = 90.0,
                        score = 9000.0,
                        formula = Formula(id = "1")
                    ),
                    gameOver = GameOver.GAME_OVER
                )
            )
        )
    }
}
