package com.hybris.tlv.domain.usecase.event

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class EventUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncEvents() = runUnitTest {
        assertTrue(actual = dependency.get().useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        dependency.get().useCases.event.prepopulateEvents()
        assertTrue(actual = dependency.get().useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())

        reset()
        assertTrue(actual = dependency.get().useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        dependency.get().useCases.event.syncEvents()
        assertTrue(actual = dependency.get().useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())

        val ids = FakeData.events.get().map { it.id }.toSet()
        assertTrue(actual = dependency.get().useCases.event.getRandomEvent(ids = ids).isEmpty())

        val event = FakeData.events.get().first { it.parentId == null }
        assertEquals(expected = listOf(event), actual = dependency.get().useCases.event.getRandomEvent(ids = ids - event.id).filter { it.parentId == null })
    }
}
