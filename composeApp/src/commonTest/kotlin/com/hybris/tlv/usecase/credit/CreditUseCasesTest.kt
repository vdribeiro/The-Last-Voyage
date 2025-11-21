package com.hybris.tlv.usecase.credit

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.reset
import com.hybris.tlv.useCases

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get credits`() = runBlocking {
        assertTrue(actual = useCases.credit.getCredits().isEmpty())
        useCases.credit.syncCredits()
        assertTrue(actual = useCases.credit.getCredits().isNotEmpty())
    }
}
