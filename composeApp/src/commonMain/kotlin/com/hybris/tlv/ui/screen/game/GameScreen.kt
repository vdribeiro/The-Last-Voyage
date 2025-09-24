package com.hybris.tlv.ui.screen.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import com.hybris.tlv.ui.screen.game.content.ShipContent
import com.hybris.tlv.ui.screen.game.content.SystemContent
import com.hybris.tlv.ui.screen.game.content.TravelContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.ui.theme.thenIf
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun GameScreen(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()
    val defaultShip = remember {
        Ship(
            id = "",
            assignedPoints = 0,
            yearsTraveled = 0.0,
            sensorRange = 0,
            integrity = 0,
            fuel = 0,
            materials = 0,
            cryopods = 0,
        )
    }
    val ship = storeState.ship ?: defaultShip

    val travelTranslation = remember { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember { getTranslation(key = "game_screen__ship") }

    Scaffold(
        modifier = Modifier.thenIf(
            tag = GAME_SCREEN,
            maxWidth = Dp.Infinity,
            maxHeight = Dp.Infinity
        ),
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier.thenIf(tag = GAME_SCREEN_STATUS_BAR),
                hull = ship.integrity.toString(),
                fuel = ship.fuel.toString(),
                materials = ship.materials.toString(),
                cryopods = ship.cryopods.toString()
            )
        },
        bottomBar = {
            // Navigation bar for travel, system and ship status
            NavigationBar(
                modifier = Modifier.thenIf(tag = GAME_SCREEN_NAVIGATION_BAR)
            ) {
                NavigationBarItem(
                    modifier = Modifier.thenIf(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP),
                    icon = { Icon(imageVector = Icons.Filled.Rocket, contentDescription = shipTranslation) },
                    label = { Text(text = shipTranslation) },
                    selected = (storeState.currentContent == Content.SHIP),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SHIP)) },
                )
                NavigationBarItem(
                    modifier = Modifier.thenIf(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM),
                    icon = { Icon(imageVector = Icons.Filled.Hub, contentDescription = systemTranslation) },
                    label = { Text(text = systemTranslation) },
                    selected = (storeState.currentContent == Content.SYSTEM),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SYSTEM)) },
                )
                NavigationBarItem(
                    modifier = Modifier.thenIf(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL),
                    icon = { Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = travelTranslation) },
                    label = { Text(text = travelTranslation) },
                    selected = (storeState.currentContent == Content.TRAVEL),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.TRAVEL)) },
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.thenIf(padding = innerPadding)) {
            when (storeState.loading) {
                true -> DebouncedLinearProgressIndicator(
                    modifier = Modifier.thenIf(
                        tag = GAME_SCREEN_PROGRESS_INDICATOR,
                        maxWidth = Dp.Infinity
                    )
                )

                false -> when (storeState.currentContent) {
                    Content.SHIP -> ShipContent(store = store)
                    Content.SYSTEM -> SystemContent(store = store)
                    Content.TRAVEL -> TravelContent(store = store)
                }
            }
        }
    }
}
