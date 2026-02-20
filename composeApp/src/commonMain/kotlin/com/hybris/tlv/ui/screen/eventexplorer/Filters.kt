package com.hybris.tlv.ui.screen.eventexplorer

import com.hybris.tlv.domain.usecase.event.model.Event
import com.hybris.tlv.domain.usecase.translation.TranslationCache

internal fun List<Event>.search(search: String): List<Event> =
    if (search.isNotBlank()) {
        val searchLowercase = search.lowercase()
        filter { event ->
            listOfNotNull(
                TranslationCache.get(key = event.id),
                TranslationCache.get(key = event.description),
                event.parentId?.let { TranslationCache.get(key = it) }
            ).any { it.lowercase().contains(other = searchLowercase) }
        }
    } else this
