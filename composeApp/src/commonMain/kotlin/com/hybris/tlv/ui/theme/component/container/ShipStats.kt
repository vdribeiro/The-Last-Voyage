package com.hybris.tlv.ui.theme.component.container

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.space.roundTo
import com.hybris.tlv.domain.translation.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.card.StatCard
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.getTranslation

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
        val statItem = @Composable { icon: ImageVector, label: String, value: String? ->
            StatCard(
                icon = icon,
                label = label,
                value = value
            )
        }
        item { statItem(Icons.Outlined.Timer, yearsTraveledTranslation, yearsTraveled?.roundTo(decimalPlaces = 2)?.toString()) }
        item { statItem(Icons.Outlined.Radar, sensorTranslation, sensorRange?.toString()) }
        item { statItem(Icons.Outlined.Shield, integrityTranslation, integrity?.let { "$it / 100" }) }
        item { statItem(Icons.Outlined.LocalGasStation, fuelTranslation, fuel?.toString()) }
        item { statItem(Icons.Outlined.Construction, materialsTranslation, materials?.toString()) }
        item { statItem(Icons.Outlined.Hotel, cryopodsTranslation, cryopods?.toString()) }
        item { statItem(Icons.Outlined.Speed, speedTranslation, velocity?.let { "${it}c" }) }
        item { statItem(Icons.Outlined.Opacity, fuelConsumptionTranslation, fuelConsumption?.roundTo(decimalPlaces = 2)?.toString()) }
    }
}

@Preview
@Composable
private fun ShipStatsPreview() = Preview {
    InjectTranslations(
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
