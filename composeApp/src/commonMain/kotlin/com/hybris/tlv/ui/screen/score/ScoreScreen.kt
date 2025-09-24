package com.hybris.tlv.ui.screen.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator
import com.hybris.tlv.ui.theme.component.Score
import com.hybris.tlv.ui.theme.thenIf
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun ScoreScreen(store: Store<ScoreAction, ScoreState>) {
    val storeState by store.stateFlow.collectAsState()
    val expandedItems = remember { mutableStateListOf<String>() }
    val titleTranslation = remember { getTranslation(key = "score_screen__title") }

    val typography = LocalTypography.current

    Scaffold(
        modifier = Modifier.thenIf(
            maxWidth = Dp.Infinity,
            maxHeight = Dp.Infinity,
        )
    ) { innerPadding ->
        Box(modifier = Modifier.thenIf(padding = innerPadding)) {
            Column(
                modifier = Modifier.thenIf(
                    maxWidth = Dp.Infinity,
                    maxHeight = Dp.Infinity,
                    padding = PaddingValues(all = 16.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.thenIf(minHeight = 8.dp, maxHeight = 8.dp))
                Text(
                    text = titleTranslation,
                    style = typography.headlineLarge,
                )
                Spacer(modifier = Modifier.thenIf(minHeight = 32.dp, maxHeight = 32.dp))
                when {
                    storeState.loading -> DebouncedLinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    else -> LazyColumn(
                        modifier = Modifier.thenIf(
                            maxWidth = Dp.Infinity,
                            maxHeight = Dp.Infinity,
                        ),
                        contentPadding = PaddingValues(all = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
                    ) {
                        // Scores
                        items(items = storeState.gameSessions, key = { it.id }) { score ->
                            Score(
                                modifier = Modifier.thenIf(onClick = {
                                    if (expandedItems.contains(element = score.id)) expandedItems.remove(element = score.id) else expandedItems.add(element = score.id)
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
    }
}
