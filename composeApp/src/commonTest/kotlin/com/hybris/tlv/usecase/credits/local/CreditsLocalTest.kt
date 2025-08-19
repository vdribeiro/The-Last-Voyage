package com.hybris.tlv.usecase.credits.local

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.credits
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class CreditsLocalTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `write and get credits`() = runBlocking {
        assertTrue(actual = mock.creditsDao.isCreditsEmpty())
        mock.creditsDao.rewriteCredits(credits = credits)
        assertEquals(expected = credits, actual = mock.creditsDao.getCredits())
    }
}
