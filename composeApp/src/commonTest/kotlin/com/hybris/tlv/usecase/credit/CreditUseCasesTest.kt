package com.hybris.tlv.usecase.credit

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
        mock.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get credits`() = runBlocking {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        mock.useCases.credit.syncCredits()
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }

    @Test
    fun `prepopulate and get credits`() = runBlocking {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        mock.useCases.credit.prepopulateCredits()
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }
}
