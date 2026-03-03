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
import com.hybris.tlv.domain.flag.FeatureFlags.flags
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.AttributePoint
import com.hybris.tlv.ui.theme.component.button.AttributeRow
import com.hybris.tlv.ui.theme.component.card.SelectableAttribute
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun <T> ShipConfiguration(
    modifier: Modifier = Modifier,
    remainingPoints: Int = 0,
    sensorRange: AttributePoint? = null,
    fuel: AttributePoint? = null,
    materials: AttributePoint? = null,
    cryopods: AttributePoint? = null,
    selectedEngineId: String? = null,
    engines: ImmutableList<T> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    description: (T) -> String? = { null },
    velocity: (T) -> Double? = { null },
    fuelConsumption: (T) -> Double? = { null },
    cost: (T) -> Int? = { null },
    onEngineClick: (T) -> Unit = {}
) {
    val shipPointsTranslation = getTranslation(key = "new_game_screen__ship_points")
    val sensorTranslation = getTranslation(key = "ship_sensor")
    val fuelTranslation = getTranslation(key = "ship_fuel")
    val materialsTranslation = getTranslation(key = "ship_materials")
    val cryopodsTranslation = getTranslation(key = "ship_cryopods")
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
            val canIncrement = remainingPoints > 0
            val attributeItem = @Composable { text: String, point: AttributePoint ->
                AttributeRow(
                    name = text,
                    canIncrement = canIncrement,
                    attributePoint = point
                )
            }
            sensorRange?.let { item { attributeItem(sensorTranslation, it) } }
            fuel?.let { item { attributeItem(fuelTranslation, it) } }
            materials?.let { item { attributeItem(materialsTranslation, it) } }
            cryopods?.let { item { attributeItem(cryopodsTranslation, it) } }
            if (flags.engines) {
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
                items(items = engines, key = id) { engine ->
                    val engineId = id(engine)
                    SelectableAttribute(
                        modifier = Modifier
                            .clickable { onEngineClick(engine) },
                        selected = selectedEngineId == engineId,
                        name = engineId,
                        description = description(engine),
                        velocity = velocity(engine),
                        fuel = fuelConsumption(engine),
                        points = cost(engine),
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
            Translation(
                key = "new_game_screen__ship_points",
                value = "Points"
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
                key = "new_game_screen__engine_select",
                value = "Engine"
            ),
            Translation(
                key = "new_game_screen__engine_speed",
                value = "Speed"
            ),
            Translation(
                key = "new_game_screen__engine_fuel",
                value = "Fuel"
            ),
        )
    )
    ShipConfiguration(
        remainingPoints = 10,
        sensorRange = AttributePoint(),
        cryopods = AttributePoint(),
        selectedEngineId = "1",
        engines = persistentListOf(
            "Engine 1",
            "Engine 2",
            "Engine 3"
        ),
        id = { it },
        description = { it },
        velocity = { 10.0 },
        fuelConsumption = { 10.0 },
        cost = { 10 }
    )
}