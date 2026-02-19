package com.hybris.tlv.ui.screen.eventexplorer

import com.hybris.tlv.domain.usecase.event.model.Event

internal data class EventExplorerState(
    val loading: Boolean = true,
    val events: List<Event> = emptyList()
)
