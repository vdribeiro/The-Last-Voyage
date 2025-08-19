package com.hybris.tlv.usecase.credit.remote

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.credits
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class CreditRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get credits`() = runBlocking {
        assertEquals(expected = Result.Success(list = emptyList()), actual = mock.creditApi.getCredits().last())
        assertEquals(expected = SyncResult.Success, actual = mock.creditApi.rewriteCredits(credits = credits).last())
        assertEquals(expected = Result.Success(list = credits), actual = mock.creditApi.getCredits().last())
    }
}
