package com.hybris.tlv.usecase.earth

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.catastrophes
import kotlin.test.Test
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class EarthUseCasesTest {

    private val mock by lazy {
        Mock()
    }
    private val useCases = mock.useCases.earth

    @Test
    fun rewrite() = runBlocking {
        useCases.rewrite().last()
        println(catastrophes)
    }
}
