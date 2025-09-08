package com.hybris.tlv.usecase.event

import com.hybris.tlv.database.EventSchema
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.space.model.TravelOutcome
import database.AppDatabase

internal class EventGateway(
    database: AppDatabase
): EventUseCases {

    private val eventDao = database.eventQueries

    override suspend fun getRandomEvent(ids: Set<String>): List<Event> {
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

    private fun EventSchema.toEvent(): Event =
        Event(
            id = id,
            description = description,
            parentId = parentId,
            outcome = outcome?.let { json.decodeFromString<TravelOutcome>(string = it) }
        )
}
