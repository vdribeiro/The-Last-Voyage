package com.hybris.tlv.usecase.event.remote

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.events
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventRemoteTest {

    @Test
    fun `get events`() = runBlocking {
        assertEquals(expected = Result.Success(list = events), actual = mock.eventApi.getEvents())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.eventApi.getEvents() is Result.Error)
    }
}
