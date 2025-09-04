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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.component.StatusBar
import com.hybris.tlv.ui.screen.game.content.ShipContent
import com.hybris.tlv.ui.screen.game.content.SystemContent
import com.hybris.tlv.ui.screen.game.content.TravelContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun GameScreen(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()
    val gameSession = storeState.gameSession
    val ship = gameSession?.ship
    val travelTranslation = remember { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember { getTranslation(key = "game_screen__ship") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            StatusBar(
                modifier = Modifier.statusBarsPadding(),
                hull = ship?.integrity?.toString() ?: "0",
                fuel = ship?.fuel?.toString() ?: "0",
                materials = ship?.materials?.toString() ?: "0",
                cryopods = ship?.cryopods?.toString() ?: "0"
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = travelTranslation) },
                    label = { Text(text = travelTranslation) },
                    selected = (storeState.currentContent == Content.TRAVEL),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.TRAVEL)) },
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Hub, contentDescription = systemTranslation) },
                    label = { Text(text = systemTranslation) },
                    selected = (storeState.currentContent == Content.SYSTEM),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SYSTEM)) },
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Rocket, contentDescription = shipTranslation) },
                    label = { Text(text = shipTranslation) },
                    selected = (storeState.currentContent == Content.SHIP),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SHIP)) },
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (gameSession) {
                null -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                else -> when (storeState.currentContent) {
                    Content.TRAVEL -> TravelContent(store = store)
                    Content.SYSTEM -> SystemContent(store = store)
                    Content.SHIP -> ShipContent(store = store)
                }
            }
        }
    }
}
