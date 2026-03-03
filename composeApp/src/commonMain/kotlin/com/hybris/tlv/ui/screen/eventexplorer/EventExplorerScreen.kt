package com.hybris.tlv.ui.screen.eventexplorer

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.usecase.event.model.Event
import com.hybris.tlv.domain.usecase.space.model.TravelOutcome
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.list.EventList
import com.hybris.tlv.ui.theme.component.text.Input

@OptIn(FlowPreview::class)
@Composable
internal fun EventExplorerScreen(store: Store<EventExplorerState, EventExplorerAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        loading = storeState.loading,
        onHelpClick = null,
        topBar = {
            val typography = LocalTypography.current
            var searchQuery by remember { mutableStateOf(value = storeState.search) }
            LaunchedEffect(key1 = Unit) {
                snapshotFlow { searchQuery }
                    .debounce(timeoutMillis = 300L)
                    .distinctUntilChanged()
                    .collect { store.send(action = EventExplorerAction.Search(it)) }
            }
            Input(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp
                    )
                    .defaultMinSize(minHeight = 60.dp),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                maxLines = 1,
                style = typography.bodyLarge
            )
        }
    ) {
        EventList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            events = storeState.events,
            id = Event::id,
            parentId = Event::parentId,
            description = Event::description,
            outcome = { it.outcome?.toStringOutcome() }
        )
    }
}

@Preview
@Composable
private fun EventExplorerScreenLoadingPreview() = Preview {
    EventExplorerScreen(
        store = Store(
            initialState = EventExplorerState(
                loading = true,
                events = persistentListOf()
            )
        )
    )
}

@Preview
@Composable
private fun EventExplorerScreenPreview() = Preview {
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
                events = persistentListOf(
                    Event(
                        id = "Engine Misfire",
                        description = "Your engine clogs unexpectedly.",
                        parentId = null,
                        outcome = null
                    ),
                    Event(
                        id = "Push the Engine",
                        description = "You can push the engine past its safety limits",
                        parentId = null,
                        outcome = null
                    ),
                    Event(
                        id = "Go for speed",
                        description = "You arrive sooner, but cause an engine strain, requiring repairs.",
                        parentId = "Push the Engine",
                        outcome = TravelOutcome(
                            integrity = -5
                        )
                    ),
                    Event(
                        id = "Travel at a normally.",
                        description = "You continue to travel as planned.",
                        parentId = "Push the Engine",
                        outcome = null
                    )
                )
            )
        )
    )
}
