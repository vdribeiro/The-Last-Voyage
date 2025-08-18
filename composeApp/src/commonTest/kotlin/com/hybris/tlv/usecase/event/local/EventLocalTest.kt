package com.hybris.tlv.usecase.event.local

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.events
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventLocalTest {

    private val mock = Mock()

    @Test
    fun `write and get events`() = runBlocking {
        assertTrue(actual = mock.eventDao.isEventEmpty())
        mock.eventDao.rewriteEvents(events = events)
        assertEquals(expected = events, actual = mock.eventDao.getEvents())
    }

    @Test
    fun `get a random event`() = runBlocking {

    }
}
