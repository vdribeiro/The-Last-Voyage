package com.hybris.tlv.usecase.learning

import com.hybris.tlv.Core
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.HttpClientFactory
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class LearningUseCasesTest {

    private val mock by lazy {
        Core(
            dispatcher = TestDispatchers(),
            sqlDriver = createSqlDriver(inMemory = true),
            httpClient = HttpClientFactory.buildHttpClient()
        )
    }

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `sync and get learnings`() = runBlocking {
        assertTrue(actual = mock.useCases.learning.getLearnings().isEmpty())
        mock.useCases.sync.sync().last()
        assertTrue(actual = mock.useCases.learning.getLearnings().isNotEmpty())
    }
}
