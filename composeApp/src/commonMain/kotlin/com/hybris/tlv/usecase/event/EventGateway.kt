package com.hybris.tlv.usecase.event

import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.event.model.Event

internal class EventGateway(
    private val eventDao: EventLocal
): EventUseCases {

    override suspend fun getEvents(): List<Event> =
        eventDao.getEvents()

    override suspend fun getRandomEvent(ids: Set<String>): List<Event> =
        eventDao.getRandomEvent(ids = ids)
}
