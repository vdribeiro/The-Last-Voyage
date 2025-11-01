package com.hybris.tlv.usecase.catastrophe

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.reset
import com.hybris.tlv.testDependency

internal class CatastropheUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get catastrophes`() = runBlocking {
        assertNull(actual = testDependency.useCases.catastrophe.getRandomCatastrophe())
        testDependency.useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = testDependency.useCases.catastrophe.getRandomCatastrophe()).let {}
    }
}
