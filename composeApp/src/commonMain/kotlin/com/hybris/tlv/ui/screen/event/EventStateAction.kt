package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Ship

internal sealed interface EventAction {
    data class Select(val event: Event): EventAction
}

internal data class EventStateBuilder(
    val gameSession: GameSession? = null,
    val eventChain: List<Event>? = null,
)

internal data class EventState(
    val ship: Ship? = null,
    val parentEvent: Event? = null,
    val childrenEvents: List<Event> = emptyList()
)
