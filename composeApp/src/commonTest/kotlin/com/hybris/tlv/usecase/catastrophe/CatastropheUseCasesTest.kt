package com.hybris.tlv.usecase.catastrophe

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.hybris.tlv.TestCase

internal class CatastropheUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncCatastrophes() = runUnitTest {
        assertNull(actual = useCases.catastrophe.getRandomCatastrophe())
        useCases.catastrophe.prepopulateCatastrophes()
        assertNotNull(actual = useCases.catastrophe.getRandomCatastrophe())

        reset()
        assertNull(actual = useCases.catastrophe.getRandomCatastrophe())
        useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = useCases.catastrophe.getRandomCatastrophe())
    }
}
