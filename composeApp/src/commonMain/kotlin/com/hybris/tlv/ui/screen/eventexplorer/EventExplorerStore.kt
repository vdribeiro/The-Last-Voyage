package com.hybris.tlv.ui.screen.eventexplorer

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.event.EventUseCases
import com.hybris.tlv.domain.usecase.event.model.Event
import com.hybris.tlv.ui.screen.Store

internal class EventExplorerStore(
    private val eventUseCases: EventUseCases
): Store<EventExplorerState, EventExplorerAction>(
    initialState = EventExplorerState()
) {
    private val eventsFlow: MutableStateFlow<List<Event>> = MutableStateFlow(value = emptyList())

    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        observeEvents()

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeEvents() {
        eventUseCases.observeEvents()
            .observe(id = "observeEvents") { events ->
                eventsFlow.value = events
                updateState { it.copy(loading = false) }
            }

        val criteriaFlow = stateFlow
            .map { state -> FilterEventsCriteria(search = state.search) }
            .distinctUntilChanged()

        combine(
            flow = criteriaFlow,
            flow2 = eventsFlow
        ) { criteria, events ->
            FilterEventsCriteriaCombine(
                criteria = criteria,
                events = events
            )
        }
            .mapLatest { criteriaCombine ->
                FilterEventsCriteriaResult(
                    events = criteriaCombine.events.search(
                        search = criteriaCombine.criteria.search
                    ).toPersistentList()
                )
            }
            .flowOn(context = Dispatcher.Default)
            .observe(id = "filterEvents") { result ->
                updateState { it.copy(events = result.events) }
            }
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
