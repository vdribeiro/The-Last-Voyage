package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.ship.model.Ship

internal sealed interface EventAction {
    data class Select(val event: Event): EventAction
}

internal data class EventState(
    val loading: Boolean,
    val ship: Ship?,
    val parentEvent: Event,
    val childrenEvents: List<Event>
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
