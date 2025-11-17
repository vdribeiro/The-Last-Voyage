package com.hybris.tlv.usecase.catastrophe

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset

internal class CatastropheUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get catastrophes`() = runBlocking {
        assertNull(actual = getUseCases().catastrophe.getRandomCatastrophe())
        getUseCases().catastrophe.syncCatastrophes()
        assertNotNull(actual = getUseCases().catastrophe.getRandomCatastrophe()).let {}
    }
}
