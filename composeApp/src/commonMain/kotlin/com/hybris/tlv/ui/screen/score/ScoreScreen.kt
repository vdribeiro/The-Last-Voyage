package com.hybris.tlv.ui.screen.score

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.locale.now
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.getStore
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.list.ScoreList
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Formula

@Composable
internal fun ScoreScreen(store: Store<ScoreState, Unit>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        ScoreList(
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
private fun ScoreLoadingPreview() = AppTheme {
    ScoreScreen(
        store = getStore(
            initialState = ScoreState(
                loading = true,
                gameSessions = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun ScoreListPreview() = AppTheme {
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
