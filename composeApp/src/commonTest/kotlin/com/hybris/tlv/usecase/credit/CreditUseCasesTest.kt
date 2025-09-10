package com.hybris.tlv.usecase.credit

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class CreditUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `sync and get credits`() = runBlocking {
        assertTrue(actual = mock.useCases.credit.getCredits().isEmpty())
        mock.useCases.sync.sync().last()
        assertTrue(actual = mock.useCases.credit.getCredits().isNotEmpty())
    }
}
