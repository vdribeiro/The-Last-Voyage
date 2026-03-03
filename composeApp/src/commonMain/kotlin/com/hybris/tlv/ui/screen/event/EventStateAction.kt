package com.hybris.tlv.ui.screen.event

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.usecase.event.model.Event
import com.hybris.tlv.domain.usecase.ship.model.Ship

internal sealed interface EventAction {
    data class Select(val event: Event): EventAction
}

internal data class EventState(
    val loading: Boolean = true,
    val ship: Ship? = null,
    val parentEvent: Event? = null,
    val childrenEvents: ImmutableList<Event> = persistentListOf(),
)

internal val defaultEvent = Event(
    id = "event__default",
    description = "event__default_description",
    parentId = null,
    outcome = null,
)
internal val stopEvent = Event(
    id = "event__default_continue",
    description = "event__default_continue",
    parentId = null,
    outcome = null,
)
