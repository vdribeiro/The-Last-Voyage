package com.hybris.tlv.ui.screen.eventexplorer

import com.hybris.tlv.domain.usecase.event.model.Event

internal sealed interface EventExplorerAction {
    data class Search(val search: String): EventExplorerAction
}

internal data class EventExplorerState(
    val loading: Boolean = true,
    val search: String = "",
    val events: List<Event> = emptyList()
)

internal data class FilterCriteria(
    val search: String,
    val events: List<Event>
)
