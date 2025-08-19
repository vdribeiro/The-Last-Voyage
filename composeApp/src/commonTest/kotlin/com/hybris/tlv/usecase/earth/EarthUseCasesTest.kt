package com.hybris.tlv.usecase.earth

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
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
    fun `rewrite and sync catastrophes`() = runBlocking {
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isEmpty())
        mock.internalEarth.rewriteCatastrophes().last()
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isNotEmpty())
        mock.clearDatabase()
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isEmpty())
        mock.internalEarth.syncCatastrophes().last()
        assertTrue(actual = mock.useCases.earth.getCatastrophes().isNotEmpty())
    }
}
