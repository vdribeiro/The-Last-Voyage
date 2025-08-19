package com.hybris.tlv.usecase.credit

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest

internal class CreditUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }
}
