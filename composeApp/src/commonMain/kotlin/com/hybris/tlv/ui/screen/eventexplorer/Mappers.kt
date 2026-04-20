package com.hybris.tlv.ui.screen.eventexplorer

import kotlinx.collections.immutable.toPersistentList
import com.hybris.tlv.domain.event.Event
import com.hybris.tlv.domain.usecase.translation.TranslationCache

internal fun EventExplorerState.toFilterEventsCriteria(): FilterEventsCriteria =
    FilterEventsCriteria(search = search)

internal fun FilterEventsCriteriaCombine.toFilterEventsCriteriaResult() =
    FilterEventsCriteriaResult(
        events = events.search(
            search = criteria.search
        ).toPersistentList()
    )

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
