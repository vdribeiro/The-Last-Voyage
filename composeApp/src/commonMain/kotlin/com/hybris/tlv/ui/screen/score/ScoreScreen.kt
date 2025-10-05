package com.hybris.tlv.ui.screen.score

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hybris.tlv.locale.now
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.Score
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ScoreScreen(store: Store<ScoreState, Unit>) {
    val storeState by store.stateFlow.collectAsState()
    val expandedItems = remember { mutableStateListOf<String>() }
    val titleTranslation = remember { getTranslation(key = "score_screen__title") }

    val typography = LocalTypography.current

    Screen(
        modifier = Modifier.testTag(tag = SCORE_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(
                modifier = Modifier.testTag(tag = SCORE_SCREEN_TITLE),
                text = titleTranslation,
                style = typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(height = 32.dp))
            LazyColumn(
                modifier = Modifier
                    .testTag(tag = SCORE_SCREEN_SCORES)
                    .fillMaxSize(),
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {
                // Scores
                items(items = storeState.gameSessions, key = { it.id }) { score ->
                    Score(
                        modifier = Modifier
                            .testTag(tag = SCORE_SCREEN_SCORE)
                            .clickable(onClick = {
                                if (expandedItems.contains(element = score.id)) {
                                    expandedItems.remove(element = score.id)
                                } else expandedItems.add(element = score.id)
                            }),
                        isExpanded = expandedItems.contains(element = score.id),
                        score = (score.score?.roundTo(decimalPlaces = 2) ?: 0.0).toString(),
                        utc = score.utc,
                        yearsTraveled = score.ship.yearsTraveled.roundTo(decimalPlaces = 2).toString(),
                        sensorRange = score.ship.sensorRange.toString(),
                        integrity = score.ship.integrity.toString(),
                        materials = score.ship.materials.toString(),
                        fuel = score.ship.fuel.toString(),
                        cryopods = score.ship.cryopods.toString()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScoreLoading() = AppTheme {
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
private fun ScoreList() = AppTheme {
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
                    )
                )
            )
        )
    )
}
