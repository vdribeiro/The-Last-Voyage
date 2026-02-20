package com.hybris.tlv.domain.usecase.catastrophe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class CatastropheUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncCatastrophes() = runUnitTest {
        assertNull(actual = dependency.get().useCases.catastrophe.getRandomCatastrophe())
        assertTrue(actual = dependency.get().useCases.catastrophe.getCatastrophes().isEmpty())
        dependency.get().useCases.catastrophe.prepopulateCatastrophes()
        assertNotNull(actual = dependency.get().useCases.catastrophe.getRandomCatastrophe())
        assertEquals(expected = FakeData.catastrophes.get().sortedBy { it.id }, actual = dependency.get().useCases.catastrophe.getCatastrophes().sortedBy { it.id })

        resetData()
        assertNull(actual = dependency.get().useCases.catastrophe.getRandomCatastrophe())
        assertTrue(actual = dependency.get().useCases.catastrophe.getCatastrophes().isEmpty())
        dependency.get().useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = dependency.get().useCases.catastrophe.getRandomCatastrophe())
        assertEquals(expected = FakeData.catastrophes.get().sortedBy { it.id }, actual = dependency.get().useCases.catastrophe.getCatastrophes().sortedBy { it.id })
    }
}
