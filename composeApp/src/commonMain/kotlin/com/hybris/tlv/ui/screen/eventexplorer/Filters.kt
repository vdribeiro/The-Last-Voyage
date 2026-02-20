package com.hybris.tlv.ui.screen.eventexplorer

import com.hybris.tlv.domain.usecase.event.model.Event

internal fun List<Event>.search(search: String): List<Event> =
    if (search.isNotBlank()) {
        val searchLowercase = search.lowercase()
        filter { event ->
            listOfNotNull(
                event.id,
                event.description,
                event.parentId,
            ).any { it.lowercase().contains(other = searchLowercase) }
        }
    } else this
