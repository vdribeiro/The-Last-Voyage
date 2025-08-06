package com.hybris.tlv.usecase.event.local

import com.hybris.tlv.usecase.event.mapper.toEvent
import com.hybris.tlv.usecase.event.mapper.toEventSchema
import com.hybris.tlv.usecase.event.model.Event
import database.AppDatabase

internal class EventDao(
    database: AppDatabase
): EventLocal {

    private val eventDao = database.eventQueries

    override fun isEventEmpty(): Boolean =
        eventDao.isEventEmpty().executeAsList().isEmpty()

    override fun rewriteEvents(events: List<Event>) = eventDao.transaction {
        eventDao.truncateEvent()
        events.forEach { eventDao.upsertEvent(Event = it.toEventSchema()) }
    }

    override fun getEvents(): List<Event> =
        eventDao.getEvents().executeAsList().map { it.toEvent() }

    override fun getRandomEvent(ids: Set<String>): List<Event> {
        val event = eventDao.getRandomEvent(ids = ids).executeAsOneOrNull()?.toEvent() ?: return emptyList()
        val treeNodes = mutableListOf(event)
        val nodesToVisit = mutableListOf(event.id)
        while (nodesToVisit.isNotEmpty()) {
            val currentParentId = nodesToVisit.removeFirstOrNull() ?: continue
            val children = eventDao.getChildEvents(parentId = currentParentId).executeAsList().map { it.toEvent() }
            if (children.isNotEmpty()) {
                treeNodes.addAll(elements = children)
                nodesToVisit.addAll(elements = children.map { it.id })
            }
        }
        return treeNodes
    }
}
