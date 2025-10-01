package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.events
import com.hybris.tlv.locale.now
import com.hybris.tlv.ship
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.translation.TranslationCache

@Preview
@Composable
private fun ScoreLoading() {
    TranslationCache.set(translations = translations)
    AppTheme {
        ScoreScreen(
            store = getStore(
                initialState = ScoreState(
                    loading = true,
                    gameSessions = emptyList()
                )
            )
        )
    }
}

@Preview
@Composable
private fun ScoreList() {
    TranslationCache.set(translations = translations)
    AppTheme {
        ScoreScreen(
            store = getStore(
                initialState = ScoreState(
                    loading = false,
                    gameSessions = listOf(gameSessionFinished)
                )
            )
        )
    }
}

private val gameSessionFinished: GameSession by lazy {
    GameSession(
        id = "2",
        utc = now(),
        currentStellarHostId = stellarHosts.random().id,
        visitedStellarHosts = stellarHosts.shuffled().take(n = 2).map { it.id }.toSet(),
        launchedEvents = events.shuffled().take(n = 2).map { it.id }.toSet(),
        settledPlanetId = "earth",
        finalHabitability = 90.0,
        score = 9000.0,
        ship = ship,
        formula = Formula(id = "1")
    )
}