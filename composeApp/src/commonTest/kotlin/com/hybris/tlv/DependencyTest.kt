package com.hybris.tlv

import kotlin.test.Test
import com.hybris.tlv.test.TestCase

internal class DependencyTest: TestCase() {

    @Test
    fun noOpDependency() = runUnitTest {
        val noOpDependency = Dependency()
        noOpDependency.useCases.sync.sync(reset = true)
    }
}
