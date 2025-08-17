package com.hybris.tlv.usecase.earth

import com.hybris.tlv.mock.Mock
import kotlin.test.Test
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class EarthUseCasesTest {

    private val mock = Mock()
    private val useCases = mock.useCases.earth

    @Test
    fun rewrite() = runBlocking {
        useCases.rewrite().last()
        Unit
    }
}
