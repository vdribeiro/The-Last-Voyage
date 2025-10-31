package com.hybris.tlv.usecase.credit

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.reset()
    }

    @Test
    fun `sync and get credits`() = runBlocking {
        assertTrue(actual = testDependency.useCases.credit.getCredits().isEmpty())
        testDependency.useCases.credit.syncCredits()
        assertTrue(actual = testDependency.useCases.credit.getCredits().isNotEmpty())
    }
}
