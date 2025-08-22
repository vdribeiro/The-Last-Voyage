package com.hybris.tlv.usecase.event

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.events
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and get events`() = runBlocking {
        assertTrue(actual = mock.useCases.event.getEvents().isEmpty())
        mock.internalEvent.prepopulateEvents()
        assertTrue(actual = mock.useCases.event.getEvents().isNotEmpty())

        assertTrue(actual = mock.useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = mock.useCases.event.getRandomEvent(ids = ids).isEmpty())
        assertEquals(
            expected = listOf(events.first()),
            actual = mock.useCases.event.getRandomEvent(ids = ids - events.first().id)
        )
    }

    @Test
    fun `prepopulate and sync events`() = runBlocking {
        assertTrue(actual = mock.useCases.event.getEvents().isEmpty())
        mock.internalEvent.syncEvents()
        assertTrue(actual = mock.useCases.event.getEvents().isNotEmpty())
    }
}
