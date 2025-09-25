package com.hybris.tlv.ui.screen.game.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SHIP_CONTENT
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SHIP_CONTENT_CRYOPODS
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SHIP_CONTENT_FUEL
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SHIP_CONTENT_INTEGRITY
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SHIP_CONTENT_MATERIALS
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SHIP_CONTENT_SENSOR
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SHIP_CONTENT_SPEED
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SHIP_CONTENT_YEARS_TRAVELED
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.StatDisplay
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun ShipContent(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = storeState.ship ?: return
    val yearsTraveledTranslation = remember { getTranslation(key = "ship_years_traveled") }
    val sensorTranslation = remember { getTranslation(key = "ship_sensor") }
    val speedTranslation = remember { getTranslation(key = "ship_speed") }
    val integrityTranslation = remember { getTranslation(key = "ship_integrity") }
    val fuelTranslation = remember { getTranslation(key = "ship_fuel") }
    val materialsTranslation = remember { getTranslation(key = "ship_materials") }
    val cryopodsTranslation = remember { getTranslation(key = "ship_cryopods") }

    // Ship status with years traveled, sensor range, maximum speed, integrity, fuel, materials and cryopods
    LazyColumn(
        modifier = Modifier
            .testTag(tag = GAME_SCREEN_SHIP_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_YEARS_TRAVELED),
                icon = Icons.Outlined.Timer,
                label = yearsTraveledTranslation,
                value = ship.yearsTraveled.roundTo(decimalPlaces = 2).toString()
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_SENSOR),
                icon = Icons.Outlined.Radar,
                label = sensorTranslation,
                value = ship.sensorRange.toString()
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_SPEED),
                icon = Icons.Outlined.Speed,
                label = speedTranslation,
                value = "0.1c" // TODO: use engine speed - using 0.1c for now
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_INTEGRITY),
                icon = Icons.Outlined.Shield,
                label = integrityTranslation,
                value = "${ship.integrity} / 100",
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_FUEL),
                icon = Icons.Outlined.LocalGasStation,
                label = fuelTranslation,
                value = ship.fuel.toString()
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_MATERIALS),
                icon = Icons.Outlined.Construction,
                label = materialsTranslation,
                value = ship.materials.toString()
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_CRYOPODS),
                icon = Icons.Outlined.BedroomParent,
                label = cryopodsTranslation,
                value = ship.cryopods.toString()
            )
        }
    }
}
