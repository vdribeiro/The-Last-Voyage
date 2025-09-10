package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.AppLogo
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator
import com.hybris.tlv.ui.theme.component.debouncedClickable
import com.hybris.tlv.ui.theme.typography
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun LearnContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarExplorerTranslation = remember { getTranslation(key = "main_menu_screen__stellar_explorer") }
    val hostDefinitionTranslation = remember { getTranslation(key = "main_menu_screen__host_definition") }
    val planetDefinitionTranslation = remember { getTranslation(key = "main_menu_screen__planet_definition") }
    val habitabilityTranslation = remember { getTranslation(key = "main_menu_screen__habitability") }
    val mechanicsTranslation = remember { getTranslation(key = "main_menu_screen__mechanics") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { AppLogo() }
        item { Spacer(modifier = Modifier.height(height = 32.dp)) }
        if (storeState.loading != false) {
            item { DebouncedLinearProgressIndicator() }
            return@LazyColumn
        }
        if (storeState.featureStellarExplorer != false) {
            item {
                Text(
                    modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.StellarExplorer) },
                    text = stellarExplorerTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.HostDefinition) },
                text = hostDefinitionTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.PlanetDefinition) },
                text = planetDefinitionTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.Habitability) },
                text = habitabilityTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier.debouncedClickable { store.send(action = MainMenuAction.Mechanics) },
                text = mechanicsTranslation,
                style = typography.headlineMedium,
            )
        }
    }
}