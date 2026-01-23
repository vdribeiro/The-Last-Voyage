package com.hybris.tlv.domain.usecase.event

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class EventUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncEvents() = runUnitTest {
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isEmpty())
        getUseCases().event.prepopulateEvents()
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isNotEmpty())

        reset()
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isEmpty())
        getUseCases().event.syncEvents()
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isNotEmpty())

        val ids = FakeData.getEvents().map { it.id }.toSet()
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = ids).isEmpty())

        val event = FakeData.getEvents().first { it.parentId == null }
        assertEquals(expected = listOf(event), actual = getUseCases().event.getRandomEvent(ids = ids - event.id).filter { it.parentId == null })
    }
}
