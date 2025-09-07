package com.hybris.tlv.ui.screen.game

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.screen.game.content.ShipContent
import com.hybris.tlv.ui.screen.game.content.SystemContent
import com.hybris.tlv.ui.screen.game.content.TravelContent
import com.hybris.tlv.ui.screen.game.content.TutorialContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.ui.theme.component.debouncedClickable
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun GameScreen(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = storeState.gameSession?.ship
    val tutorial = storeState.tutorial != Tutorial.NO
    val travelTranslation = remember { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember { getTranslation(key = "game_screen__ship") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .debouncedClickable(
                enabled = storeState.tutorial != Tutorial.NO,
                rippleEffect = false
            ) { store.send(action = GameAction.Next) },
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier.statusBarsPadding(),
                hull = ship?.integrity?.toString() ?: (if (tutorial) (0..100).random() else 0).toString(),
                fuel = ship?.fuel?.toString() ?: (if (tutorial) (0..100).random() else 0).toString(),
                materials = ship?.materials?.toString() ?: (if (tutorial) (0..100).random() else 0).toString(),
                cryopods = ship?.cryopods?.toString() ?: (if (tutorial) (0..100).random() else 0).toString()
            )
        },
        bottomBar = {
            // Navigation bar for travel, system and ship status
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Rocket, contentDescription = shipTranslation) },
                    label = { Text(text = shipTranslation) },
                    selected = (storeState.currentContent == Content.SHIP || storeState.tutorial == Tutorial.SHIP),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SHIP)) },
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Hub, contentDescription = systemTranslation) },
                    label = { Text(text = systemTranslation) },
                    selected = (storeState.currentContent == Content.SYSTEM || storeState.tutorial == Tutorial.SYSTEM),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SYSTEM)) },
                )
                NavigationBarItem(
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
                true -> DebouncedLinearProgressIndicator()
                false -> when (storeState.currentContent) {
                    Content.TUTORIAL -> TutorialContent(store = store)
                    Content.SHIP -> ShipContent(store = store)
                    Content.SYSTEM -> SystemContent(store = store)
                    Content.TRAVEL -> TravelContent(store = store)
                }
            }
        }
    }
}
