package com.hybris.tlv.usecase.learning

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.reset
import com.hybris.tlv.testDependency

internal class LearningUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get learnings`() = runBlocking {
        assertTrue(actual = testDependency.useCases.learning.getLearnings().isEmpty())
        testDependency.useCases.learning.syncLearnings()
        assertTrue(actual = testDependency.useCases.learning.getLearnings().isNotEmpty())
    }
}
