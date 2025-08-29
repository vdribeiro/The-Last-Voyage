package com.hybris.tlv.usecase.event.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class EventRemoteTest {

    @Test
    fun `get events`() = runTest {
        assertEquals(expected = Result.Success(list = events), actual = mock.eventApi.getEvents())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.eventApi.getEvents() is Result.Error)
    }
}
