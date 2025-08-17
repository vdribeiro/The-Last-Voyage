package com.hybris.tlv.usecase.event.local

import com.hybris.tlv.Tester
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.events
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventLocalTest: Tester() {

    @Test
    fun `write and get events`() = runBlocking {
        assertTrue(actual = eventDao.isEventEmpty())
        eventDao.rewriteEvents(events = events)
        assertEquals(expected = events, actual = eventDao.getEvents())
    }
}
