package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.PropertyCard
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun PropertiesContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHostProperties = storeState.learningsMap[LearningType.HOST_PROPERTY].orEmpty()
    val planetProperties = storeState.learningsMap[LearningType.PLANET_PROPERTY].orEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = getTranslation(key = "stellar_explorer_screen__host_list"),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        items(items = stellarHostProperties, key = { it.id }) { property ->
            PropertyCard(
                name = property.id,
                description = property.description,
            )
        }
        item {
            Text(
                text = getTranslation(key = "stellar_explorer_screen__planet_list"),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        items(items = planetProperties, key = { it.id }) { property ->
            PropertyCard(
                name = property.id,
                description = property.description,
            )
        }
    }
}
