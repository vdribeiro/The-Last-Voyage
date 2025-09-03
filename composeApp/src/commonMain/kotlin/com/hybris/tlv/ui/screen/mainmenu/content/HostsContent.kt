package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.StellarHostCard
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.mapper.spectralTypeToDrawable
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun HostsContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHosts = storeState.learningsMap[LearningType.HOST_TYPE].orEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = stellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                name = getTranslation(key = stellarHost.id),
                description = stellarHost.description,
                spectralTypeDrawable = stellarHost.image.spectralTypeToDrawable(),
            )
        }
    }
}
