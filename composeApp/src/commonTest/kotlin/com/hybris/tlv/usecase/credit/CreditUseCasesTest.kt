package com.hybris.tlv.usecase.credit

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get credits`() = runBlocking {
        assertTrue(actual = testDependency.useCases.credit.getCredits().isEmpty())
        testDependency.useCases.credit.syncCredits()
        assertTrue(actual = testDependency.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `prepopulate and get credits`() = runBlocking {
        assertTrue(actual = testDependency.useCases.credit.getCredits().isEmpty())
        testDependency.useCases.credit.prepopulateCredits()
        assertTrue(actual = testDependency.useCases.credit.getCredits().isNotEmpty())
    }
}
