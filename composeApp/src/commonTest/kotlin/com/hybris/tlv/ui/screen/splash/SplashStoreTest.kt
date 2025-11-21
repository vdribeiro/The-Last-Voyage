package com.hybris.tlv.ui.screen.splash

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getSplashStore
import com.hybris.tlv.reset

internal class SplashStoreTest {

    private val store: SplashStore get() = getSplashStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
    }

    @Test
    fun `init`() = runBlocking {
        val splashStore = store
        delay(timeMillis = 100L)
        assertEquals(expected = 1f, actual = splashStore.stateFlow.value.progress)
    }
}
