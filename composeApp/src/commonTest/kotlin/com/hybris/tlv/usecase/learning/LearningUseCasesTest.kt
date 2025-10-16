package com.hybris.tlv.usecase.learning

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency

internal class LearningUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get learnings`() = runBlocking {
        assertTrue(actual = testDependency.useCases.learning.getLearnings().isEmpty())
        testDependency.useCases.learning.syncLearnings()
        assertTrue(actual = testDependency.useCases.learning.getLearnings().isNotEmpty())
    }
}
