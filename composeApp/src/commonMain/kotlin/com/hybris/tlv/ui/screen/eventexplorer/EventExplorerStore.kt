package com.hybris.tlv.ui.screen.eventexplorer

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.event.EventUseCases
import com.hybris.tlv.ui.screen.Store

internal class EventExplorerStore(
    private val eventUseCases: EventUseCases
): Store<EventExplorerState, EventExplorerAction>(
    initialState = EventExplorerState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val eventsFlow = eventUseCases.observeEvents()
            .toStateFlow(initialValue = emptyList())

        val criteriaFlow = stateFlow
            .map { it.toFilterEventsCriteria() }
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
            .mapLatest { it.toFilterEventsCriteriaResult() }
            .flowOn(context = Dispatcher.Default)
            .observe(id = "filterEvents") { result ->
                updateState {
                    it.copy(
                        loading = false,
                        events = result.events
                    )
                }
            }

        Telemetry.info(tag = TAG, message = "Setup complete")
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
