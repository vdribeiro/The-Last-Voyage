package com.hybris.tlv.usecase.earth

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class EarthUseCasesTest {

    private val mock by lazy {
        Mock()
    }

    @BeforeTest
    fun setup() = runBlocking {
    }

    @Test
    fun rewrite() = runBlocking {
        mock.useCases.sync.prepopulate().last()
        println(mock.useCases.earth.getCatastrophes())
    }
}
