package com.hybris.tlv.ui.screen.eventexplorer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.usecase.event.model.Event
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.list.EventList

@Composable
internal fun EventExplorerScreen(store: Store<EventExplorerState, Unit>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        store = store,
        loading = storeState.loading,
    ) {
        EventList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            events = storeState.events,
            id = { it.id },
            description = { it.description }
        )
    }
}

@Preview
@Composable
private fun EventExplorerScreenLoadingPreview() = AppTheme {
    EventExplorerScreen(
        store = Store(
            initialState = EventExplorerState(
                loading = true,
                events = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun EventExplorerScreenPreview() = AppTheme {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "event_screen__title",
                value = "Events"
            ),
        )
    )
    EventExplorerScreen(
        store = Store(
            initialState = EventExplorerState(
                loading = false,
                events = listOf(
                    Event(
                        id = "Asteroid",
                        description = "Go Boom!",
                        parentId = null,
                        outcome = null
                    ),
                    Event(
                        id = "Volcano",
                        description = "Go Bam!",
                        parentId = null,
                        outcome = null
                    )
                )
            )
        )
    )
}
