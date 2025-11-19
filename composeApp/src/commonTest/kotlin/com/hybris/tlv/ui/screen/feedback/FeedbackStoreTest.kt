package com.hybris.tlv.ui.screen.feedback

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.reset
import com.hybris.tlv.ui.navigation.Screen

internal class FeedbackStoreTest {

    private val store: FeedbackStore get() = getStoreFactory().createFeedbackStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Feedback))
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
        getNavigation().back()
        assertEquals(expected = SplashScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
