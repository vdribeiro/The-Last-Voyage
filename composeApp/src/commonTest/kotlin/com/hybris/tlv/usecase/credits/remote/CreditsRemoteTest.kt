package com.hybris.tlv.usecase.credits.remote

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.credits
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class CreditsRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get credits`() = runBlocking {
        assertEquals(expected = Result.Success(list = emptyList()), actual = mock.creditsApi.getCredits().last())
        assertEquals(expected = SyncResult.Success, actual = mock.creditsApi.rewriteCredits(credits = credits).last())
        assertEquals(expected = Result.Success(list = credits), actual = mock.creditsApi.getCredits().last())
    }
}
