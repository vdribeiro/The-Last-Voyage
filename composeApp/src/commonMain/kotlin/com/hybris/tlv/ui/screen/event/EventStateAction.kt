package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal data class EventState(
    val gameSession: GameSession? = null,
    val events: List<Event> = emptyList(),
    val event: Event? = null,
    val children: List<Event> = emptyList()
)

internal sealed interface EventAction {
    data class Select(val event: Event?): EventAction
}
