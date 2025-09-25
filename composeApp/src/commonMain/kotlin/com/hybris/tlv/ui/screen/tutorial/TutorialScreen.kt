package com.hybris.tlv.ui.screen.tutorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.ui.theme.debouncedClickable
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun TutorialScreen(store: Store<TutorialAction, TutorialState>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = remember {
        Ship(
            id = "",
            assignedPoints = 0,
            yearsTraveled = 0.0,
            sensorRange = (1..5).random(),
            integrity = (50..100).random(),
            fuel = (50..1000).random(),
            materials = (50..1000).random(),
            cryopods = (50..1000).random(),
        )
    }

    val travelTranslation = remember { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember { getTranslation(key = "game_screen__ship") }

    val typography = LocalTypography.current

    Scaffold(
        modifier = Modifier
            .testTag(tag = TUTORIAL_SCREEN)
            .fillMaxSize(),
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier
                    .testTag(tag = TUTORIAL_SCREEN_STATUS_BAR)
                    .statusBarsPadding(),
                hull = ship.integrity.toString(),
                fuel = ship.fuel.toString(),
                materials = ship.materials.toString(),
                cryopods = ship.cryopods.toString()
            )
        },
        bottomBar = {
            // Navigation bar for travel, system and ship status
            NavigationBar(
                modifier = Modifier
                    .testTag(tag = TUTORIAL_SCREEN_NAVIGATION_BAR)
            ) {
                NavigationBarItem(
                    modifier = Modifier
                        .testTag(tag = TUTORIAL_SCREEN_NAVIGATION_BAR_ITEM_SHIP),
                    icon = { Icon(imageVector = Icons.Filled.Rocket, contentDescription = shipTranslation) },
                    label = { Text(text = shipTranslation) },
                    selected = (storeState.tutorialStep == Tutorial.SHIP),
                    onClick = { },
                )
                NavigationBarItem(
                    modifier = Modifier
                        .testTag(tag = TUTORIAL_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM),
                    icon = { Icon(imageVector = Icons.Filled.Hub, contentDescription = systemTranslation) },
                    label = { Text(text = systemTranslation) },
                    selected = (storeState.tutorialStep == Tutorial.SYSTEM),
                    onClick = { },
                )
                NavigationBarItem(
                    modifier = Modifier
                        .testTag(tag = TUTORIAL_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL),
                    icon = { Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = travelTranslation) },
                    label = { Text(text = travelTranslation) },
                    selected = (storeState.tutorialStep == Tutorial.TRAVEL),
                    onClick = { },
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .testTag(tag = TUTORIAL_SCREEN_CONTENT)
                .debouncedClickable(rippleEffect = false) { store.send(action = TutorialAction.Next) }
                .semantics(mergeDescendants = false) {}
                .padding(paddingValues = innerPadding)
        ) {
            val title: String
            val description: String
            when (storeState.tutorialStep) {
                Tutorial.GOAL -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_goal_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_goal_description") }
                }

                Tutorial.SHIP -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_attributes_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_attributes_description") }
                }

                Tutorial.SYSTEM -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_system_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_system_description") }
                }

                Tutorial.TRAVEL -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_travel_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_travel_description") }
                }

                Tutorial.GAME_OVER -> {
                    title = remember { getTranslation(key = "tutorial_screen__mechanics_game_over_title") }
                    description = remember { getTranslation(key = "tutorial_screen__mechanics_game_over_description") }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 64.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(all = 8.dp),
                    style = typography.titleLarge,
                    text = title,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.padding(all = 8.dp),
                    style = typography.titleMedium,
                    text = description,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}
