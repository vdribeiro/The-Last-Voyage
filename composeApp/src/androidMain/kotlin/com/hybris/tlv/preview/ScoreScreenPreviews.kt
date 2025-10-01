package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.locale.now
import com.hybris.tlv.ui.screen.score.ScoreScreen
import com.hybris.tlv.ui.screen.score.ScoreState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Formula

@Preview
@Composable
private fun ScoreLoading() {
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
    AppTheme {
        ScoreScreen(
            store = getStore(
                initialState = ScoreState(
                    loading = false,
                    gameSessions = listOf(
                        GameSession(
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
                        )
                    )
                )
            )
        )
    }
}
