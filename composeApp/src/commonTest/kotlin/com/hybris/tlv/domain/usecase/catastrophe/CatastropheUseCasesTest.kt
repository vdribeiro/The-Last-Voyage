package com.hybris.tlv.domain.usecase.catastrophe

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.hybris.tlv.test.TestCase

internal class CatastropheUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncCatastrophes() = runUnitTest {
        assertNull(actual = getUseCases().catastrophe.getRandomCatastrophe())
        getUseCases().catastrophe.prepopulateCatastrophes()
        assertNotNull(actual = getUseCases().catastrophe.getRandomCatastrophe())

        reset()
        assertNull(actual = getUseCases().catastrophe.getRandomCatastrophe())
        getUseCases().catastrophe.syncCatastrophes()
        assertNotNull(actual = getUseCases().catastrophe.getRandomCatastrophe())
    }
}
