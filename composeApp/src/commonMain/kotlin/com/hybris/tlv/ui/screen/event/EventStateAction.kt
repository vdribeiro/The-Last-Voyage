package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Ship

internal sealed interface EventAction {
    data class Select(val event: Event): EventAction
}

internal data class EventState(
    val loading: Boolean,
    val ship: Ship?,
    val parentEvent: Event?,
    val childrenEvents: List<Event>
)
