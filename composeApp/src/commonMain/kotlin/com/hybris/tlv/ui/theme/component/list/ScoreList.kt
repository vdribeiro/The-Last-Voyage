package com.hybris.tlv.ui.theme.component.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.core.locale.getLocalDateTime
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.ScoreCard
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun <T> ScoreList(
    modifier: Modifier = Modifier,
    scores: ImmutableList<T> = persistentListOf(),
    expandedItems: ImmutableList<String> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    scorePoints: (T) -> Double? = { null },
    utc: (T) -> String? = { null },
    settledPlanet: (T) -> String? = { null },
    habitability: (T) -> Double? = { null },
    assignedPoints: (T) -> Int? = { null },
    yearsTraveled: (T) -> Double? = { null },
    sensorRange: (T) -> Int? = { null },
    integrity: (T) -> Int? = { null },
    fuel: (T) -> Int? = { null },
    materials: (T) -> Int? = { null },
    cryopods: (T) -> Int? = { null }
) {
    val expandedItems = remember { expandedItems.toMutableStateList() }

    val titleTranslation = getTranslation(key = "score_screen__title")

    val typography = LocalTypography.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = titleTranslation,
            style = typography.headlineMedium,
        )
        LazyColumn(
            modifier = Modifier
                .testTag(tag = "score_list")
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            items(items = scores, key = id) { score ->
                val scoreId = id(score)
                ScoreCard(
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
private fun ScoreListPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "score_screen__title",
                value = "Score"
            ),
            Translation(
                key = "ship_sensor",
                value = "Sensor Range"
            ),
            Translation(
                key = "ship_cryopods",
                value = "Cryopods"
            ),
            Translation(
                key = "engine",
                value = "Engine"
            ),
            Translation(
                key = "points",
                value = "Points"
            ),
        )
    )
    ScoreList(
        scores = persistentListOf(
            "Score 1",
            "Score 2",
            "Score 3",
        ),
        expandedItems = persistentListOf("Score 2"),
        id = { it },
        scorePoints = { 100.0 },
        utc = { getLocalDateTime() },
        assignedPoints = { 10 },
        sensorRange = { 1 },
        cryopods = { 100 }
    )
}