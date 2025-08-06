package com.hybris.tlv.ui.screen.explore.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.Option
import com.hybris.tlv.ui.screen.explore.ExploreAction
import com.hybris.tlv.ui.screen.explore.ExploreState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun MenuContent(store: Store<ExploreAction, ExploreState>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(height = 48.dp))
        Option(
            text = getTranslation(key = "explore_screen__mechanics"),
            onClick = { store.send(action = ExploreAction.Mechanics) }
        )
        Option(
            text = getTranslation(key = "explore_screen__habitability"),
            onClick = { store.send(action = ExploreAction.Habitability) }
        )
        Option(
            text = getTranslation(key = "explore_screen__planet_types"),
            onClick = { store.send(action = ExploreAction.PlanetTypes) }
        )
    }
}
