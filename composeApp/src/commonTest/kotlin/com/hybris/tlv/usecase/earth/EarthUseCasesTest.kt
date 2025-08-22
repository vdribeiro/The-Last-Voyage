package com.hybris.tlv.usecase.earth

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EarthUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and get catastrophes`() = runBlocking {
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isEmpty())
        mock.internalEarth.prepopulateCatastrophes()
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync catastrophes`() = runBlocking {
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isEmpty())
        mock.internalEarth.syncCatastrophes()
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isNotEmpty())
    }
}
