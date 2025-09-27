package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Ship

internal sealed interface EventAction {
    data class Select(val event: Event): EventAction
}

internal sealed interface EventStateBuilder {
    data object Default: EventStateBuilder
    data class FromState(
        val state: EventState,
        val gameSession: GameSession?,
        val eventChain: List<Event>
    ): EventStateBuilder
}

internal data class EventState(
    val loading: Boolean = true,
    val ship: Ship? = null,
    val parentEvent: Event? = null,
    val childrenEvents: List<Event> = emptyList(),
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
