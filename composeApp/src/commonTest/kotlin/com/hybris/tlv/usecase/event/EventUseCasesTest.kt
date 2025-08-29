package com.hybris.tlv.usecase.event

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `prepopulate and get events`() = runBlocking {
        assertNull(actual = mock.useCases.event.getRandomEvent(ids = emptySet()))
        mock.internalEvent.prepopulateEvents()
        assertNotNull(actual = mock.useCases.event.getRandomEvent(ids = emptySet()))

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
        assertNull(actual = mock.useCases.event.getRandomEvent(ids = emptySet()))
        assertTrue(actual = mock.internalEvent.syncEvents() is SyncResult.Success)
        assertNotNull(actual = mock.useCases.event.getRandomEvent(ids = emptySet())).let {}
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.internalEvent.syncEvents() is SyncResult.Error)
    }
}
