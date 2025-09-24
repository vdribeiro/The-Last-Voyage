package com.hybris.tlv.ui.screen.game.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_TRAVEL_CONTENT
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_TRAVEL_CONTENT_STELLAR_HOST
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.StellarHostCard
import com.hybris.tlv.ui.theme.thenIf
import com.hybris.tlv.usecase.space.formula.spectralTypeToDrawable

@Composable
internal fun TravelContent(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier.thenIf(
            tag = GAME_SCREEN_TRAVEL_CONTENT,
            maxWidth = Dp.Infinity,
            maxHeight = Dp.Infinity,
            padding = PaddingValues(all = 16.dp)
        ),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        items(items = storeState.nearStellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                modifier = Modifier.thenIf(
                    tag = GAME_SCREEN_TRAVEL_CONTENT_STELLAR_HOST,
                    onClick = { store.send(action = GameAction.Travel(stellarHost = stellarHost)) }
                ),
                name = stellarHost.name,
                planetCount = stellarHost.planets.size,
                spectralType = stellarHost.spectralType,
                spectralTypeDrawable = stellarHost.spectralType.spectralTypeToDrawable(),
                distance = stellarHost.distance,
            )
        }
    }
}
