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
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.card.StatDisplay
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.space.roundTo
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun ShipStats(
    modifier: Modifier = Modifier,
    integrity: Int? = null,
    fuel: Int? = null,
    materials: Int? = null,
    cryopods: Int? = null,
    sensorRange: Int? = null,
    yearsTraveled: Double? = null,
    velocity: Double? = null,
    fuelConsumption: Double? = null,
) {
    val integrityTranslation = getTranslation(key = "ship_integrity")
    val fuelTranslation = getTranslation(key = "ship_fuel")
    val materialsTranslation = getTranslation(key = "ship_materials")
    val cryopodsTranslation = getTranslation(key = "ship_cryopods")
    val sensorTranslation = getTranslation(key = "ship_sensor")
    val yearsTraveledTranslation = getTranslation(key = "ship_years_traveled")
    val speedTranslation = getTranslation(key = "ship_speed")
    val fuelConsumptionTranslation = getTranslation(key = "ship_fuel_consumption")

    // Ship status with years traveled, sensor range, maximum speed, integrity, fuel, materials and cryopods
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        yearsTraveled?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.Timer,
                    label = yearsTraveledTranslation,
                    value = it.roundTo(decimalPlaces = 2).toString()
                )
            }
        }
        sensorRange?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.Radar,
                    label = sensorTranslation,
                    value = it.toString()
                )
            }
        }
        integrity?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.Shield,
                    label = integrityTranslation,
                    value = "$it / 100",
                )
            }
        }
        fuel?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.LocalGasStation,
                    label = fuelTranslation,
                    value = it.toString()
                )
            }
        }
        materials?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.Construction,
                    label = materialsTranslation,
                    value = it.toString()
                )
            }
        }
        cryopods?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.Hotel,
                    label = cryopodsTranslation,
                    value = it.toString()
                )
            }
        }
        velocity?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.Speed,
                    label = speedTranslation,
                    value = "${it}c"
                )
            }
        }
        fuelConsumption?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.Opacity,
                    label = fuelConsumptionTranslation,
                    value = it.roundTo(decimalPlaces = 2).toString()
                )
            }
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
        fuelConsumption = 35.0
    )
}
