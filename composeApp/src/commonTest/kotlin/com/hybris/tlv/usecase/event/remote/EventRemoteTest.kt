package com.hybris.tlv.usecase.event.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.events
import com.hybris.tlv.usecase.SyncResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class EventRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get events`() = runBlocking {
        assertEquals(expected = Result.Success(list = emptyList()), actual = mock.eventApi.getEvents().last())
        assertEquals(expected = SyncResult.Success, actual = mock.eventApi.rewriteEvents(events = events).last())
        assertEquals(expected = Result.Success(list = events), actual = mock.eventApi.getEvents().last())
    }
}
