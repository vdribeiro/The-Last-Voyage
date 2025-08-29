package com.hybris.tlv.usecase.credit.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class CreditRemoteTest {

    @Test
    fun `get credits`() = runTest {
        assertEquals(expected = Result.Success(list = credits), actual = mock.creditApi.getCredits())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.creditApi.getCredits() is Result.Error)
    }
}
