package com.hybris.tlv.usecase.learning

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset

internal class LearningUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get learnings`() = runBlocking {
        assertTrue(actual = getUseCases().learning.getLearnings().isEmpty())
        getUseCases().learning.syncLearnings()
        assertTrue(actual = getUseCases().learning.getLearnings().isNotEmpty())
    }
}
