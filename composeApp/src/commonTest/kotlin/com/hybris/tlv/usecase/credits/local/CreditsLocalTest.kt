package com.hybris.tlv.usecase.credits.local

import com.hybris.tlv.Tester
import com.hybris.tlv.mock.credits
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class CreditsLocalTest: Tester() {

    @Test
    fun `write and get credits`() = runBlocking {
        assertTrue(actual = creditsDao.isCreditsEmpty())
        creditsDao.rewriteCredits(credits = credits)
        assertEquals(expected = credits, actual = creditsDao.getCredits())
    }
}
