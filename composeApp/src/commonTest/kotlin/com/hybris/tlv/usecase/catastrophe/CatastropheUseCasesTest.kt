package com.hybris.tlv.usecase.catastrophe

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.reset
import com.hybris.tlv.useCases

internal class CatastropheUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get catastrophes`() = runBlocking {
        assertNull(actual = useCases.catastrophe.getRandomCatastrophe())
        useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = useCases.catastrophe.getRandomCatastrophe()).let {}
    }
}
