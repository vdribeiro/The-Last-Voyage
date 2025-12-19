package com.hybris.tlv.usecase.catastrophe

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.hybris.tlv.TestCase

internal class CatastropheUseCasesTest: TestCase() {

    @Test
    fun `sync and get catastrophes`() = runUnitTest {
        assertNull(actual = useCases.catastrophe.getRandomCatastrophe())
        useCases.catastrophe.syncCatastrophes()
        assertNotNull(actual = useCases.catastrophe.getRandomCatastrophe()).let {}
    }
}
