package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class CatastropheUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `sync and get catastrophes`() = runBlocking {
        assertNull(actual = mock.useCases.catastrophe.getRandomCatastrophe())
        mock.useCases.sync.sync().last()
        assertNotNull(actual = mock.useCases.catastrophe.getRandomCatastrophe()).let {}
    }
}
