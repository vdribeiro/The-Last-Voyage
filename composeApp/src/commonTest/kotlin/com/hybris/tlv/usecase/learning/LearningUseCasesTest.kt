package com.hybris.tlv.usecase.learning

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class LearningUseCasesTest {

    @BeforeTest
    fun setup() {
        testCore.clearDatabase()
        testCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get learnings`() = runBlocking {
        assertTrue(actual = testCore.useCases.learning.getLearnings().isEmpty())
        testCore.useCases.learning.syncLearnings()
        assertTrue(actual = testCore.useCases.learning.getLearnings().isNotEmpty())
    }

    @Test
    fun `prepopulate and get learnings`() = runBlocking {
        assertTrue(actual = testCore.useCases.learning.getLearnings().isEmpty())
        testCore.useCases.learning.prepopulateLearnings()
        assertTrue(actual = testCore.useCases.learning.getLearnings().isNotEmpty())
    }
}
