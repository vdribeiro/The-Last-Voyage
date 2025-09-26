package com.hybris.tlv.usecase.credit

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() {
        testCore.sqlDriver.clearDatabase()
        testCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get credits`() = runBlocking {
        assertTrue(actual = testCore.useCases.credit.getCredits().isEmpty())
        testCore.useCases.credit.syncCredits()
        assertTrue(actual = testCore.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `prepopulate and get credits`() = runBlocking {
        assertTrue(actual = testCore.useCases.credit.getCredits().isEmpty())
        testCore.useCases.credit.prepopulateCredits()
        assertTrue(actual = testCore.useCases.credit.getCredits().isNotEmpty())
    }
}
