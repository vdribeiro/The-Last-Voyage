package com.hybris.tlv.screen.score

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.screen.Screen
import com.hybris.tlv.screen.Store
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.theme.component.list.ScoreList
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun ScoreScreen(store: Store<ScoreState, Unit>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        store = store,
        loading = storeState.loading,
    ) {
        ScoreList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            scores = storeState.gameSessions,
            id = { it.id },
            scorePoints = { it.score },
            utc = { it.utc },
            settledPlanet = { it.settledPlanetName },
            habitability = { it.finalHabitability },
            engine = { it.ship.engine.id },
            assignedPoints = { it.ship.assignedPoints },
            yearsTraveled = { it.ship.yearsTraveled },
            sensorRange = { it.ship.sensorRange },
            integrity = { it.ship.integrity },
            fuel = { it.ship.fuel },
            materials = { it.ship.materials },
            cryopods = { it.ship.cryopods }
        )
    }
}

@Preview
@Composable
private fun ScoreScreenLoadingPreview() = AppTheme {
    ScoreScreen(
        store = Store(
            initialState = ScoreState(
                loading = true,
                gameSessions = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun ScoreScreenPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "score_screen__title",
                value = "Score"
            )
        )
    )
    ScoreScreen(
        store = Store(
            initialState = ScoreState(
                loading = false,
                gameSessions = listOf(
                    GameSession(
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
                    )
                )
            )
        )
    )
}
