package com.hybris.tlv.usecase.learning

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mockCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class LearningUseCasesTest {

    @BeforeTest
    fun setup() {
        mockCore.sqlDriver.clearDatabase()
        mockCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get learnings`() = runBlocking {
        assertTrue(actual = mockCore.useCases.learning.getLearnings().isEmpty())
        mockCore.useCases.learning.syncLearnings()
        assertTrue(actual = mockCore.useCases.learning.getLearnings().isNotEmpty())
    }

    @Test
    fun `prepopulate and get learnings`() = runBlocking {
        assertTrue(actual = mockCore.useCases.learning.getLearnings().isEmpty())
        mockCore.useCases.learning.prepopulateLearnings()
        assertTrue(actual = mockCore.useCases.learning.getLearnings().isNotEmpty())
    }
}
