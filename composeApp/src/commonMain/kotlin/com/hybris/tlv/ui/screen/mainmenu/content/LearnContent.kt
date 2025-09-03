package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.AppLogo
import com.hybris.tlv.ui.component.debouncedClickable
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun LearnContent(store: Store<MainMenuAction, MainMenuState>) {
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
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.Mechanics) },
                text = getTranslation(key = "main_menu_screen__mechanics"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.StellarExplorer) },
                text = getTranslation(key = "main_menu_screen__stellar_explorer"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.HostTypes) },
                text = getTranslation(key = "main_menu_screen__star_types"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.PlanetTypes) },
                text = getTranslation(key = "main_menu_screen__planet_types"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.Properties) },
                text = getTranslation(key = "main_menu_screen__properties"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.Habitability) },
                text = getTranslation(key = "main_menu_screen__habitability"),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}