package com.hybris.tlv.ui.screen.eventexplorer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import androidx.lifecycle.viewModelScope
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.event.EventUseCases
import com.hybris.tlv.domain.usecase.event.model.Event
import com.hybris.tlv.test.VisibleForTesting
import com.hybris.tlv.ui.screen.Store

internal class EventExplorerStore(
    private val eventUseCases: EventUseCases
): Store<EventExplorerState, EventExplorerAction>(
    initialState = EventExplorerState()
) {
    @VisibleForTesting
    internal var eventsFlow: MutableStateFlow<List<Event>> = MutableStateFlow(value = emptyList())

    init {
        setup()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        observeEvents()
        combine(
            flow = stateFlow,
            flow2 = eventsFlow
        ) { state, events ->
            FilterCriteria(
                search = state.search,
                events = events
            )
        }
            .distinctUntilChanged()
            .mapLatest { criteria -> criteria.events.search(search = criteria.search) }
            .flowOn(context = Dispatcher.Default)
            .onEach { events -> updateState { it.copy(events = events) } }
            .launchIn(scope = viewModelScope)

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun observeEvents(): Job = eventUseCases.observeEvents()
        .observe { events ->
            eventsFlow.value = events
            updateState { it.copy(loading = false) }
        }

    override fun reducer(state: EventExplorerState, action: EventExplorerAction) {
        when (action) {
            is EventExplorerAction.Search -> updateState { it.copy(search = action.search) }
        }
    }

    companion object {
        private const val TAG = "EventExplorerStore"
    }
}
