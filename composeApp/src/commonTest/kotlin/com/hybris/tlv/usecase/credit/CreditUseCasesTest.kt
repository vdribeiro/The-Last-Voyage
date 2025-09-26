package com.hybris.tlv.usecase.credit

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mockCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() {
        mockCore.sqlDriver.clearDatabase()
        mockCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get credits`() = runBlocking {
        assertTrue(actual = mockCore.useCases.credit.getCredits().isEmpty())
        mockCore.useCases.credit.syncCredits()
        assertTrue(actual = mockCore.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `prepopulate and get credits`() = runBlocking {
        assertTrue(actual = mockCore.useCases.credit.getCredits().isEmpty())
        mockCore.useCases.credit.prepopulateCredits()
        assertTrue(actual = mockCore.useCases.credit.getCredits().isNotEmpty())
    }
}
