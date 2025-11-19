package com.hybris.tlv.ui.screen.splash

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.reset
import com.hybris.tlv.ui.navigation.Screen

internal class SplashStoreTest {

    private val store: SplashStore get() = getStoreFactory().createSplashStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Splash))
    }

    @Test
    fun `init`() = runBlocking {
        val splashStore = store
        delay(timeMillis = 100L)
        assertEquals(expected = 1f, actual = splashStore.stateFlow.value.progress)
    }
}
