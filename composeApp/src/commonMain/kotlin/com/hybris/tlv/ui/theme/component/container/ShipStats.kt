package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.space.roundTo
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.card.StatDisplay
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.translation.TranslationCache
import com.hybris.tlv.ui.translation.getTranslation

@Composable
internal fun ShipStats(
    modifier: Modifier = Modifier,
    tutorial: Boolean = false,
    integrity: Int? = null,
    fuel: Int? = null,
    materials: Int? = null,
    cryopods: Int? = null,
    sensorRange: Int? = null,
    yearsTraveled: Double? = null,
    velocity: Double? = null,
    fuelConsumption: Double? = null,
) {
    val yearsTraveledTranslation = getTranslation(key = if (!tutorial) "ship_years_traveled" else "ship_years_traveled_tutorial")
    val sensorTranslation = getTranslation(key = if (!tutorial) "ship_sensor" else "ship_sensor_tutorial")
    val integrityTranslation = getTranslation(key = if (!tutorial) "ship_integrity" else "ship_integrity_tutorial")
    val fuelTranslation = getTranslation(key = if (!tutorial) "ship_fuel" else "ship_fuel_tutorial")
    val materialsTranslation = getTranslation(key = if (!tutorial) "ship_materials" else "ship_materials_tutorial")
    val cryopodsTranslation = getTranslation(key = if (!tutorial) "ship_cryopods" else "ship_cryopods_tutorial")
    val speedTranslation = getTranslation(key = if (!tutorial) "ship_speed" else "ship_speed_tutorial")
    val fuelConsumptionTranslation = getTranslation(key = if (!tutorial) "ship_fuel_consumption" else "ship_fuel_consumption_tutorial")

    // Ship status with years traveled, sensor range, maximum speed, integrity, fuel, materials and cryopods
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            StatDisplay(
                icon = Icons.Outlined.Timer,
                label = yearsTraveledTranslation,
                value = yearsTraveled?.roundTo(decimalPlaces = 2)?.toString()
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Radar,
                label = sensorTranslation,
                value = sensorRange?.toString()
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Shield,
                label = integrityTranslation,
                value = integrity?.let { "$it / 100" },
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.LocalGasStation,
                label = fuelTranslation,
                value = fuel?.toString()
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Construction,
                label = materialsTranslation,
                value = materials?.toString()
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Hotel,
                label = cryopodsTranslation,
                value = cryopods?.toString()
            )
        }

        item {
            StatDisplay(
                icon = Icons.Outlined.Speed,
                label = speedTranslation,
                value = velocity?.let { "${it}c" }
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Opacity,
                label = fuelConsumptionTranslation,
                value = fuelConsumption?.roundTo(decimalPlaces = 2)?.toString()
            )
        }
    }
}

@Preview
@Composable
private fun ShipStatsPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "ship_years_traveled",
                value = "Years Travelled"
            ),
            Translation(
                key = "ship_speed",
                value = "Speed"
            ),
            Translation(
                key = "ship_integrity",
                value = "Integrity"
            ),
            Translation(
                key = "ship_sensor",
                value = "Sensor Range"
            ),
            Translation(
                key = "ship_fuel",
                value = "Fuel"
            ),
            Translation(
                key = "ship_materials",
                value = "Materials"
            ),
            Translation(
                key = "ship_cryopods",
                value = "Cryopods"
            ),
            Translation(
                key = "ship_fuel_consumption",
                value = "Fuel Consumption"
            ),
        )
    )
    ShipStats(
        integrity = 100,
        fuel = 100,
        materials = 100,
        cryopods = 100,
        sensorRange = 1,
        yearsTraveled = 10.0,
        velocity = 1.0,
        fuelConsumption = 35.0,
    )
}
