package com.hybris.tlv.domain.usecase.catastrophe

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.hybris.tlv.test.TestCase

internal class CatastropheUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncCatastrophes() = runUnitTest {
        assertNull(actual = dependency.get().useCases.catastrophe.getRandomCatastrophe())
        dependency.get().useCases.catastrophe.prepopulateCatastrophes()
        assertNotNull(actual = dependency.get().useCases.catastrophe.getRandomCatastrophe())

        resetData()
        assertNull(actual = dependency.get().useCases.catastrophe.getRandomCatastrophe())
        dependency.get().useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = dependency.get().useCases.catastrophe.getRandomCatastrophe())
    }
}
