package com.hybris.tlv.usecase.learning

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class LearningUseCasesTest {

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
