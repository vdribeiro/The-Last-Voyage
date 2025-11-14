package com.hybris.tlv.ui.theme.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BedroomParent
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.component.card.StatDisplay
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun ShipStats(
    modifier: Modifier = Modifier,
    velocity: Double? = null,
    yearsTraveled: Double? = null,
    sensorRange: Int? = null,
    integrity: Int? = null,
    fuel: Int? = null,
    materials: Int? = null,
    cryopods: Int? = null
) {
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val yearsTraveledTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_years_traveled") }
    val sensorTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_sensor") }
    val speedTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_speed") }
    val integrityTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_integrity") }
    val fuelTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_fuel") }
    val materialsTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_materials") }
    val cryopodsTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_cryopods") }

    // Ship status with years traveled, sensor range, maximum speed, integrity, fuel, materials and cryopods
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
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
        velocity?.let {
            item {
                StatDisplay(
                    icon = Icons.Outlined.Speed,
                    label = speedTranslation,
                    value = "${it}c"
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
                    icon = Icons.Outlined.BedroomParent,
                    label = cryopodsTranslation,
                    value = it.toString()
                )
            }
        }
    }
}
