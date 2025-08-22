package com.hybris.tlv.usecase.credit.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.credits
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class CreditRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get credits`() = runBlocking {
        assertEquals(expected = Result.Success(list = credits), actual = mock.creditApi.getCredits())
    }
}
