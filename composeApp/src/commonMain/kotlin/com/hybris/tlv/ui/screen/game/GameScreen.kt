package com.hybris.tlv.ui.screen.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.game.content.ShipContent
import com.hybris.tlv.ui.screen.game.content.SystemContent
import com.hybris.tlv.ui.screen.game.content.TravelContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.ui.theme.debouncedClickable
import com.hybris.tlv.ui.theme.typography
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun GameScreen(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()
    val tutorial = storeState.tutorial != Tutorial.NO
    val defaultShip = remember {
        Ship(
            id = "",
            assignedPoints = 0,
            yearsTraveled = 0.0,
            sensorRange = if (tutorial) (1..5).random() else 0,
            integrity = if (tutorial) (50..100).random() else 0,
            fuel = if (tutorial) (50..1000).random() else 0,
            materials = if (tutorial) (50..1000).random() else 0,
            cryopods = if (tutorial) (50..1000).random() else 0,
        )
    }
    val ship = storeState.gameSession?.ship ?: defaultShip

    val travelTranslation = remember { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember { getTranslation(key = "game_screen__ship") }

    Scaffold(
        modifier = Modifier
            .testTag(tag = GAME_SCREEN)
            .fillMaxSize()
            .debouncedClickable(
                enabled = tutorial,
                rippleEffect = false
            ) { store.send(action = GameAction.NextTutorial) },
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier
                    .testTag(tag = GAME_SCREEN_STATUS_BAR)
                    .statusBarsPadding(),
                //hullEnabled = storeState.tutorial != Tutorial.NO,
                //fuelEnabled = storeState.tutorial != Tutorial.NO,
                //materialsEnabled = storeState.tutorial != Tutorial.NO,
                //cryopodsEnabled = storeState.tutorial != Tutorial.NO,
                hull = ship.integrity.toString(),
                fuel = ship.fuel.toString(),
                materials = ship.materials.toString(),
                cryopods = ship.cryopods.toString()
            )
        },
        bottomBar = {
            // Navigation bar for travel, system and ship status
            NavigationBar {
                NavigationBarItem(
                    modifier = Modifier
                        .testTag(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP),
                    icon = { Icon(imageVector = Icons.Filled.Rocket, contentDescription = shipTranslation) },
                    label = { Text(text = shipTranslation) },
                    selected = (storeState.currentContent == Content.SHIP || storeState.tutorial == Tutorial.SHIP),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SHIP)) },
                )
                NavigationBarItem(
                    modifier = Modifier
                        .testTag(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM),
                    icon = { Icon(imageVector = Icons.Filled.Hub, contentDescription = systemTranslation) },
                    label = { Text(text = systemTranslation) },
                    selected = (storeState.currentContent == Content.SYSTEM || storeState.tutorial == Tutorial.SYSTEM),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SYSTEM)) },
                )
                NavigationBarItem(
                    modifier = Modifier
                        .testTag(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL),
                    icon = { Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = travelTranslation) },
                    label = { Text(text = travelTranslation) },
                    selected = (storeState.currentContent == Content.TRAVEL || storeState.tutorial == Tutorial.TRAVEL),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.TRAVEL)) },
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (storeState.loading) {
                null, true -> DebouncedLinearProgressIndicator(
                    modifier = Modifier
                        .testTag(tag = GAME_SCREEN_PROGRESS_INDICATOR)
                        .fillMaxWidth()
                )
                false -> when (storeState.currentContent) {
                    null -> {}
                    Content.SHIP -> ShipContent(store = store)
                    Content.SYSTEM -> SystemContent(store = store)
                    Content.TRAVEL -> TravelContent(store = store)
                }
            }

            val title: String
            val description: String
            when (storeState.tutorial) {
                null, Tutorial.NO -> {
                    title = remember { "" }
                    description = remember { "" }
                }

                Tutorial.YES -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_goal_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_goal_description") }
                }

                Tutorial.SHIP -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_attributes_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_attributes_description") }
                }

                Tutorial.TRAVEL -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_travel_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_travel_description") }
                }

                Tutorial.SYSTEM -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_game_over_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_game_over_description") }
                }
                //Tutorial.SYSTEM -> {
                //    title = remember { getTranslation(key = "tutorial_screen__mechanics_score_title") }
                //    description = remember { getTranslation(key = "tutorial_screen__mechanics_score_description") }
                //}
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(all = 8.dp),
                    style = typography.titleLarge,
                    text = title,
                )
                Text(
                    modifier = Modifier.padding(all = 8.dp),
                    style = typography.titleMedium,
                    text = description,
                )
            }
        }
    }
}
