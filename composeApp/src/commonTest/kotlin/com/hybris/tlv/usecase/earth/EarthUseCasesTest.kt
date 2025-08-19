package com.hybris.tlv.usecase.earth

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest

internal class EarthUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }
}
