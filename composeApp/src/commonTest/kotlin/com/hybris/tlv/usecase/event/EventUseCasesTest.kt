package com.hybris.tlv.usecase.event

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.events

internal class EventUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncEvents() = runUnitTest {
        assertTrue(actual = useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        useCases.event.prepopulateEvents()
        assertTrue(actual = useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())

        reset()
        assertTrue(actual = useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        useCases.event.syncEvents()
        assertTrue(actual = useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())

        val ids = events.map { it.id }.toSet()
        assertTrue(actual = useCases.event.getRandomEvent(ids = ids).isEmpty())

        val event = events.first { it.parentId == null }
        assertEquals(expected = listOf(element = event), actual = useCases.event.getRandomEvent(ids = ids - event.id).filter { it.parentId == null })
    }
}
