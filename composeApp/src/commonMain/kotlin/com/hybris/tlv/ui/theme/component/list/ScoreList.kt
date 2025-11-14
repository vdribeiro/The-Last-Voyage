package com.hybris.tlv.ui.theme.component.list

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.locale.now
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.Score
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal inline fun <T> ScoreList(
    modifier: Modifier = Modifier,
    scores: List<T> = emptyList(),
    expandedItems: List<String> = emptyList(),
    noinline id: (T) -> String = { generateUuid() },
    crossinline scorePoints: (T) -> Double? = { null },
    crossinline utc: (T) -> String? = { null },
    crossinline settledPlanet: (T) -> String? = { null },
    crossinline habitability: (T) -> Double? = { null },
    crossinline engine: (T) -> String? = { null },
    crossinline assignedPoints: (T) -> Int? = { null },
    crossinline yearsTraveled: (T) -> Double? = { null },
    crossinline sensorRange: (T) -> Int? = { null },
    crossinline integrity: (T) -> Int? = { null },
    crossinline fuel: (T) -> Int? = { null },
    crossinline materials: (T) -> Int? = { null },
    crossinline cryopods: (T) -> Int? = { null }
) {
    val expandedItems = remember { expandedItems.toMutableStateList() }

    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val titleTranslation = remember(key1 = translationVersion) { getTranslation(key = "score_screen__title") }

    val typography = LocalTypography.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = titleTranslation,
            style = typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            // Scores
            items(items = scores, key = id) { score ->
                val scoreId = id(score)
                Score(
                    modifier = Modifier
                        .clickable(onClick = {
                            if (expandedItems.contains(element = scoreId)) {
                                expandedItems.remove(element = scoreId)
                            } else expandedItems.add(element = scoreId)
                        }),
                    isExpanded = expandedItems.contains(element = scoreId),
                    score = scorePoints(score),
                    utc = utc(score),
                    settledPlanet = settledPlanet(score),
                    habitability = habitability(score),
                    engine = engine(score),
                    assignedPoints = assignedPoints(score),
                    yearsTraveled = yearsTraveled(score),
                    sensorRange = sensorRange(score),
                    integrity = integrity(score),
                    materials = materials(score),
                    fuel = fuel(score),
                    cryopods = cryopods(score),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ScoreListPreview() = AppTheme {
    ScoreList(
        scores = listOf(
            "Score 1",
            "Score 2",
            "Score 3",
        ),
        expandedItems = listOf("Score 2"),
        scorePoints = { 100.0 },
        utc = { now() },
        settledPlanet = { "Earth" },
        habitability = { 80.0 },
        engine = { "BFE" },
        assignedPoints = { 10 },
        yearsTraveled = { 10.0 },
        sensorRange = { 1 },
        integrity = { 100 },
        materials = { 100 },
        fuel = { 100 },
        cryopods = { 100 }
    )
}