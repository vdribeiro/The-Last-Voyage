package com.hybris.tlv.ui.screen.eventexplorer

import kotlinx.coroutines.Job
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

        val events = eventUseCases.getEvents()
        updateState {
            it.copy(
                loading = false,
                events = events
            )
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
