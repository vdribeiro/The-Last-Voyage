package com.hybris.tlv.ui.screen.score

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.Score
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.mapper.roundTo
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ScoreScreen(store: Store<ScoreAction, ScoreState>) {
    val storeState by store.stateFlow.collectAsState()
    val expandedItems = remember { mutableStateListOf<String>() }

    BackHandler(enabled = true) { store.send(action = ScoreAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(height = 8.dp))
                Text(
                    text = getTranslation(key = "score_screen__title"),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(height = 32.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 12.dp)
                ) {
                    items(items = storeState.scores, key = { it.id }) { score ->
                        val isExpanded = expandedItems.contains(element = score.id)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .clickable(onClick = { if (isExpanded) expandedItems.remove(element = score.id) else expandedItems.add(element = score.id) })
                                    .padding(all = 16.dp)
                            ) {
                                Score(
                                    isExpanded = isExpanded,
                                    score = (score.score?.roundTo(decimalPlaces = 2) ?: 0.0).toString(),
                                    utc = score.utc,
                                    yearsTraveled = score.yearsTraveled.roundTo(decimalPlaces = 2).toString(),
                                    sensorRange = score.sensorRange.toString(),
                                    integrity = score.integrity.toString(),
                                    materials = score.materials.toString(),
                                    fuel = score.fuel.toString(),
                                    cryopods = score.cryopods.toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
