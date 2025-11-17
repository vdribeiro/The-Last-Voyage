package com.hybris.tlv.usecase.credit

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get credits`() = runBlocking {
        assertTrue(actual = getUseCases().credit.getCredits().isEmpty())
        getUseCases().credit.syncCredits()
        assertTrue(actual = getUseCases().credit.getCredits().isNotEmpty())
    }
}
