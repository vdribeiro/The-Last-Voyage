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
import com.hybris.tlv.ui.component.PlanetCard
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.mapper.toDrawable
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun PlanetsContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val planets = storeState.learningsMap[LearningType.PLANET].orEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = planets, key = { it.id }) { planet ->
            PlanetCard(
                name = getTranslation(key = planet.id),
                description = planet.description,
                typeDrawable = PlanetType.fromValue(value = planet.image.orEmpty()).toDrawable()
            )
        }
    }
}
