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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.ui.component.StatusBar
import com.hybris.tlv.ui.screen.game.content.ShipContent
import com.hybris.tlv.ui.screen.game.content.SystemContent
import com.hybris.tlv.ui.screen.game.content.TravelContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun GameScreen(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()

    BackHandler(enabled = true) { store.send(action = GameAction.Back) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            StatusBar(
                modifier = Modifier.statusBarsPadding(),
                hull = storeState.gameSession?.integrity?.toString().orEmpty(),
                fuel = storeState.gameSession?.fuel?.toString().orEmpty(),
                materials = storeState.gameSession?.materials?.toString().orEmpty(),
                cryopods = storeState.gameSession?.cryopods?.toString().orEmpty()
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = getTranslation(key = "game_screen__travel")) },
                    label = { Text(text = getTranslation(key = "game_screen__travel")) },
                    selected = (storeState.currentContent == Content.TRAVEL),
                    onClick = { store.send(action = GameAction.ChangeContent(content = Content.TRAVEL)) },
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Hub, contentDescription = getTranslation(key = "game_screen__system")) },
                    label = { Text(text = getTranslation(key = "game_screen__system")) },
                    selected = (storeState.currentContent == Content.SYSTEM),
                    onClick = { store.send(action = GameAction.ChangeContent(content = Content.SYSTEM)) },
                )
                NavigationBarItem(
                    icon = { Icon(imageVector = Icons.Filled.Rocket, contentDescription = getTranslation(key = "game_screen__ship")) },
                    label = { Text(text = getTranslation(key = "game_screen__ship")) },
                    selected = (storeState.currentContent == Content.SHIP),
                    onClick = { store.send(action = GameAction.ChangeContent(content = Content.SHIP)) },
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (storeState.currentContent) {
                null -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Content.TRAVEL -> TravelContent(store = store)
                Content.SYSTEM -> SystemContent(store = store)
                Content.SHIP -> ShipContent(store = store)
            }
        }
    }
}
