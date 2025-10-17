package com.hybris.tlv.ui.screen.gameover

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.locale.now
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.bottombar.Snackbar
import com.hybris.tlv.ui.theme.component.card.Score
import com.hybris.tlv.ui.theme.component.container.TypewriterContent
import com.hybris.tlv.ui.theme.component.screen.Screen
import com.hybris.tlv.usecase.gamesession.model.GameOver
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun GameOverScreen(store: Store<GameOverState, GameOverAction>) {
    val storeState by store.stateFlow.collectAsState()
    val gameSession = storeState.gameSession
    val ship = gameSession?.ship

    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val gameOverTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_over_screen__game_over") }
    val messageTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_over_screen__score") }
    val scoreTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_over_screen__end") }
    val newAchievementTranslation = remember(key1 = translationVersion) { getTranslation(key = "achievements_screen__new") }

    Screen(
        modifier = Modifier.testTag(tag = GAME_OVER_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            ButtonsBar(
                buttons = listOf(
                    when (storeState.currentContent) {
                        Content.MESSAGE -> messageTranslation
                        Content.SCORE -> scoreTranslation
                    } to { store.send(action = GameOverAction.Next) }
                )
            )
        },
        snackbarHost = {
            if (storeState.showAchievements) Snackbar(messages = remember(key1 = storeState.achievements) {
                storeState.achievements.map { achievement ->
                    "$newAchievementTranslation: ${getTranslation(key = achievement.id)}"
                }
            })
        }
    ) {
        when (storeState.currentContent) {
            Content.MESSAGE -> TypewriterContent(
                title = gameOverTranslation,
                text = storeState.gameOver?.displayName?.let { getTranslation(key = it) },
            )

            Content.SCORE -> TypewriterContent(title = gameOverTranslation) {
                if (gameSession != null && ship != null) Score(
                    modifier = Modifier.testTag(tag = GAME_OVER_SCREEN_SCORE),
                    score = (gameSession.score?.roundTo(decimalPlaces = 2) ?: 0.0).toString(),
                    utc = gameSession.utc,
                    yearsTraveled = ship.yearsTraveled.roundTo(decimalPlaces = 2).toString(),
                    sensorRange = ship.sensorRange.toString(),
                    integrity = ship.integrity.toString(),
                    materials = ship.materials.toString(),
                    fuel = ship.fuel.toString(),
                    cryopods = ship.cryopods.toString(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameOverLoadingPreview() = AppTheme {
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

@Preview
@Composable
private fun GameOverMessagePreview() = AppTheme {
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

@Preview
@Composable
private fun GameOverScorePreview() = AppTheme {
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
