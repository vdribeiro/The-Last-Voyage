package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.locale.now
import com.hybris.tlv.ui.screen.gameover.Content
import com.hybris.tlv.ui.screen.gameover.GameOverScreen
import com.hybris.tlv.ui.screen.gameover.GameOverState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

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
