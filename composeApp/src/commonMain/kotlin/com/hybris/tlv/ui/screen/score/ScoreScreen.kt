package com.hybris.tlv.ui.screen.score

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.core.locale.getLocalDateTime
import com.hybris.tlv.domain.usecase.gamesession.model.GameSession
import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.Ship
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.list.ScoreList

@Composable
internal fun ScoreScreen(store: Store<ScoreState, Unit>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        loading = storeState.loading,
    ) {
        ScoreList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            scores = storeState.gameSessions,
            id = GameSession::id,
            scorePoints = GameSession::score,
            utc = GameSession::utc,
            settledPlanet = GameSession::settledPlanetName,
            habitability = GameSession::finalHabitability,
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
private fun ScoreScreenLoadingPreview() = Preview {
    ScoreScreen(
        store = Store(
            initialState = ScoreState(
                loading = true,
                gameSessions = persistentListOf()
            )
        )
    )
}

@Preview
@Composable
private fun ScoreScreenPreview() = Preview {
    InjectTranslations(
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
                gameSessions = persistentListOf(
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
