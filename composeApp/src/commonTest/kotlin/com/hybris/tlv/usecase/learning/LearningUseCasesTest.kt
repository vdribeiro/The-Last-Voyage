package com.hybris.tlv.usecase.learning

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class LearningUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
        mock.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get learnings`() = runBlocking {
        assertTrue(actual = mock.useCases.learning.getLearnings().isEmpty())
        mock.useCases.learning.syncLearnings()
        assertTrue(actual = mock.useCases.learning.getLearnings().isNotEmpty())
    }

    @Test
    fun `prepopulate and get learnings`() = runBlocking {
        assertTrue(actual = mock.useCases.learning.getLearnings().isEmpty())
        mock.useCases.learning.prepopulateLearnings()
        assertTrue(actual = mock.useCases.learning.getLearnings().isNotEmpty())
    }
}
