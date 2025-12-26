package com.hybris.tlv.screen.splash

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.TestCase

internal class SplashStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        val store = storeFactory.getSplashStore(reset = true)
        assertEquals(expected = 1f, actual = store.stateFlow.value.progress)
    }
}
