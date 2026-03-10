package com.hybris.tlv.ui.screen.eventexplorer

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.usecase.event.model.Event

internal sealed interface EventExplorerAction {
    data class Search(val search: String): EventExplorerAction
}

internal data class EventExplorerState(
    val loading: Boolean = true,
    val search: String = "",
    val events: ImmutableList<Event> = persistentListOf()
)

internal data class FilterEventsCriteria(
    val search: String,
)

internal data class FilterEventsCriteriaCombine(
    val criteria: FilterEventsCriteria,
    val events: List<Event>
)

internal data class FilterEventsCriteriaResult(
    val events: ImmutableList<Event>
)
