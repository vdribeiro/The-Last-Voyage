package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.AppLogo
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun MainMenuContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { Spacer(modifier = Modifier.height(height = 16.dp)) }
        item { AppLogo() }
        item { Spacer(modifier = Modifier.height(height = 64.dp)) }
        item {
            Text(
                modifier = Modifier.clickable { store.send(action = MainMenuAction.NewGame) },
                text = getTranslation(key = "main_menu_screen__new_game"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        if (storeState.ongoingGameSession) {
            item {
                Text(
                    modifier = Modifier.clickable { store.send(action = MainMenuAction.Continue) },
                    text = getTranslation(key = "main_menu_screen__continue"),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
        item {
            Text(
                modifier = Modifier.clickable { store.send(action = MainMenuAction.Learn) },
                text = getTranslation(key = "main_menu_screen__learn"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.clickable { store.send(action = MainMenuAction.Scores) },
                text = getTranslation(key = "main_menu_screen__scores"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        //Text(
        //    modifier = Modifier.clickable { store.send(action = MainMenuAction.Achievements) },
        //    text = getTranslation(key = "main_menu_screen__achievements"),
        //    style = MaterialTheme.typography.headlineMedium,
        //)
        item {
            Text(
                modifier = Modifier.clickable { store.send(action = MainMenuAction.Credits) },
                text = getTranslation(key = "main_menu_screen__credits"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}
