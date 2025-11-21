package com.hybris.tlv.ui.screen.feedback

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getFeedbackStore
import com.hybris.tlv.reset

internal class FeedbackStoreTest {

    private val store: FeedbackStore get() = getFeedbackStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Feedback))
    }

    @Test
    fun `send action back`() = runBlocking {
        store
//        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//        assertEquals(expected = SplashScreen, actual = getNavigation().stateFlow.value.screen)
    }
}
