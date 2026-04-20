package com.hybris.tlv.ui.theme.component.container

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.AttributeRow
import com.hybris.tlv.ui.theme.component.card.SelectableCard
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun <A, E> ShipConfiguration(
    modifier: Modifier = Modifier,
    remainingPoints: Int = 0,
    attributes: ImmutableList<A> = persistentListOf(),
    attributeName: (A) -> String = { it.hashCode().toString() },
    attributeValue: (A) -> Int? = { null },
    attributeCanIncrement: (A) -> Boolean = { true },
    attributeCanDecrement: (A) -> Boolean = { true },
    onAttributeIncrement: (A) -> Unit = {},
    onAttributeDecrement: (A) -> Unit = {},
    selectedEngineId: String? = null,
    engines: ImmutableList<E> = persistentListOf(),
    engineId: (E) -> String = { it.hashCode().toString() },
    engineName: @Composable (E) -> String? = { null },
    engineDescription: @Composable (E) -> String? = { null },
    engineVelocity: (E) -> Double? = { null },
    engineFuelConsumption: (E) -> Double? = { null },
    engineCost: (E) -> Int? = { null },
    onEngineClick: (E) -> Unit = {}
) {
    val shipPointsTranslation = getTranslation(key = "new_game_screen__ship_points")
    val engineSelectTranslation = getTranslation(key = "new_game_screen__engine_select")

    val typography = LocalTypography.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // Remaining points
        InfoRow(
            modifier = Modifier
                .padding(bottom = 16.dp),
            label = shipPointsTranslation,
            value = remainingPoints,
            textAlign = TextAlign.Center,
            style = typography.headlineMedium,
        )

        // Attributes for sensor range, fuel, materials and cryopods
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(weight = 1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 4.dp, alignment = Alignment.CenterVertically),
        ) {
            items(items = attributes, key = attributeName) { attribute ->
                AttributeRow(
                    name = attributeName(attribute),
                    value = attributeValue(attribute),
                    canIncrement = attributeCanIncrement(attribute),
                    canDecrement = attributeCanDecrement(attribute),
                    onIncrement = { onAttributeIncrement(attribute) },
                    onDecrement = { onAttributeDecrement(attribute) }
                )
            }
            if (engines.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        text = engineSelectTranslation,
                        style = typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }
                items(items = engines, key = engineId) { engine ->
                    SelectableCard(
                        modifier = Modifier
                            .clickable { onEngineClick(engine) },
                        selected = selectedEngineId == engineId(engine),
                        name = engineName(engine),
                        description = engineDescription(engine),
                        velocity = engineVelocity(engine),
                        fuel = engineFuelConsumption(engine),
                        points = engineCost(engine),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ShipConfigurationPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "new_game_screen__ship_points",
                value = "Points"
            ),
            PreviewTranslation(
                key = "ship_sensor",
                value = "Sensor Range"
            ),
            PreviewTranslation(
                key = "ship_cryopods",
                value = "Cryopods"
            ),
            PreviewTranslation(
                key = "new_game_screen__engine_select",
                value = "Engine"
            ),
            PreviewTranslation(
                key = "new_game_screen__engine_speed",
                value = "Speed"
            ),
            PreviewTranslation(
                key = "new_game_screen__engine_fuel",
                value = "Fuel"
            ),
        )
    )
    ShipConfiguration(
        remainingPoints = 10,
        attributes = persistentListOf(
            "Sensor Range" to 3,
            "Cryopods" to 100
        ),
        attributeName = { it.first },
        attributeValue = { it.second },
        selectedEngineId = "1",
        engines = persistentListOf(
            "Engine 1",
            "Engine 2",
            "Engine 3"
        ),
        engineId = { it },
        engineName = { it },
        engineDescription = { it },
        engineVelocity = { 10.0 },
        engineFuelConsumption = { 10.0 },
        engineCost = { 10 }
    )
}