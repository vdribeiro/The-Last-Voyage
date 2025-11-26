package com.hybris.tlv.ui.theme.component.list

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.AttributePoint
import com.hybris.tlv.ui.theme.component.button.AttributeRow
import com.hybris.tlv.ui.theme.component.card.SelectableAttribute
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal inline fun <T> ShipConfiguration(
    modifier: Modifier = Modifier,
    remainingPoints: Int = 0,
    sensorRange: AttributePoint? = null,
    fuel: AttributePoint? = null,
    materials: AttributePoint? = null,
    cryopods: AttributePoint? = null,
    selectedEngineId: String? = null,
    engines: List<T> = emptyList(),
    noinline id: (T) -> String = { generateUuid() },
    crossinline description: (T) -> String? = { null },
    crossinline velocity: (T) -> Double? = { null },
    crossinline fuelConsumption: (T) -> Double? = { null },
    crossinline cost: (T) -> Int? = { null },
    crossinline onEngineClick: (T) -> Unit = {}
) {
    val shipPointsTranslation = getTranslation(key = "new_game_screen__ship_points")
    val sensorTranslation = getTranslation(key = "ship_sensor")
    val fuelTranslation = getTranslation(key = "ship_fuel")
    val materialsTranslation = getTranslation(key = "ship_materials")
    val cryopodsTranslation = getTranslation(key = "ship_cryopods")
    val engineSelectTranslation = getTranslation(key = "new_game_screen__engine_select")

    val typography = LocalTypography.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Remaining points
        InfoRow(
            modifier = Modifier.padding(bottom = 16.dp),
            label = shipPointsTranslation,
            value = remainingPoints,
            textAlign = TextAlign.Center,
            style = typography.headlineMedium,
        )

        // Attributes for sensor range, fuel, materials and cryopods
        LazyColumn(
            modifier = Modifier
                .padding(all = 16.dp)
                .weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val canIncrement = remainingPoints > 0
            sensorRange?.let {
                item {
                    AttributeRow(
                        name = sensorTranslation,
                        canIncrement = canIncrement,
                        attributePoint = it
                    )
                }
            }
            fuel?.let {
                item {
                    AttributeRow(
                        name = fuelTranslation,
                        canIncrement = canIncrement,
                        attributePoint = it
                    )
                }
            }
            materials?.let {
                item {
                    AttributeRow(
                        name = materialsTranslation,
                        canIncrement = canIncrement,
                        attributePoint = it
                    )
                }
            }
            cryopods?.let {
                item {
                    AttributeRow(
                        name = cryopodsTranslation,
                        canIncrement = canIncrement,
                        attributePoint = it
                    )
                }
            }
            item {
                Text(
                    modifier = Modifier.padding(all = 16.dp),
                    text = engineSelectTranslation,
                    style = typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
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

@Preview
@Composable
private fun ShipConfigurationPreview() = AppTheme {
    TranslationCache.set(
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
        engines = listOf(
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