package com.hybris.tlv.ui.screen.game.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.StellarHostCard
import com.hybris.tlv.ui.theme.component.debouncedClickable
import com.hybris.tlv.usecase.space.mapper.spectralTypeToDrawable

@Composable
internal fun TravelContent(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        items(items = storeState.nearStellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                modifier = Modifier.debouncedClickable { store.send(action = GameAction.Travel(stellarHost = stellarHost)) },
                name = stellarHost.name,
                planetCount = stellarHost.planets.size,
                spectralType = stellarHost.spectralType,
                spectralTypeDrawable = stellarHost.spectralType.spectralTypeToDrawable(),
                distance = stellarHost.distance,
            )
        }
    }
}
