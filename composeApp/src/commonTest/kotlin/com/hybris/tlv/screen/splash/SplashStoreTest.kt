package com.hybris.tlv.screen.splash

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.TestCase

internal class SplashStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        val store = storeFactory.getSplashStore(reset = true)
        store.setupJob?.join()
        assertEquals(expected = 1f, actual = store.state.progress)
        assertEquals(expected = Content.INTRO, actual = store.state.currentContent)
    }
}
