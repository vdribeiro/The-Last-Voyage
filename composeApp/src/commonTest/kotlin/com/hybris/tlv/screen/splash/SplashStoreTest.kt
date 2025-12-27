package com.hybris.tlv.screen.splash

import kotlin.test.Test
import com.hybris.tlv.TestCase

internal class SplashStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        storeFactory.getSplashStore(reset = true)
        testScheduler.advanceUntilIdle()
//        assertEquals(expected = 1f, actual = store.state.progress)
    }
}
