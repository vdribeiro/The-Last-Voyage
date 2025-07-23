package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.Option
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun MainMenuScreen(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val ongoingGameSession = storeState.ongoingGameSession ?: return

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = getTranslation(key = "app_name"),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(height = 48.dp))
                Option(
                    text = getTranslation(key = "main_menu_screen__new_game"),
                    onClick = { store.send(action = MainMenuAction.NewGame) }
                )
                if (ongoingGameSession) {
                    Option(
                        text = getTranslation(key = "main_menu_screen__continue"),
                        onClick = { store.send(action = MainMenuAction.Continue) }
                    )
                }
                Option(
                    text = getTranslation(key = "main_menu_screen__explore"),
                    onClick = { store.send(action = MainMenuAction.Explore) }
                )
                Option(
                    text = getTranslation(key = "main_menu_screen__scores"),
                    onClick = { store.send(action = MainMenuAction.Scores) }
                )
                Option(
                    text = getTranslation(key = "main_menu_screen__achievements"),
                    onClick = { store.send(action = MainMenuAction.Achievements) }
                )
                Option(
                    text = getTranslation(key = "main_menu_screen__credits"),
                    onClick = { store.send(action = MainMenuAction.Credits) }
                )
            }
        }
    }
}
