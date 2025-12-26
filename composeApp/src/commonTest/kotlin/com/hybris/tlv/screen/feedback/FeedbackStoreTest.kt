package com.hybris.tlv.screen.feedback

import kotlin.test.Test
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class FeedbackStoreTest: TestCase() {

//    private val store: FeedbackStore get() = getFeedbackStore()
//
//    @BeforeTest
//    fun setup() = runBlocking {
//        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = Screen.Feedback))
//    }
//
//    @Test
//    fun `send action back`() = runBlocking {
//        store
//        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//        assertEquals(expected = SplashScreen, actual = getNavigation().stateFlow.value.screen)
//    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        navigate(screen = Screen.Feedback())
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
        storeFactory.getFeedbackStore(tag = null, message = null).back()
        assertNavigationBackstack(list = emptyList())

        val tag = "tag"
        val message = "message"
        navigate(screen = Screen.Feedback(tag = tag, message = message))
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
        storeFactory.getFeedbackStore(tag = tag, message = message).back()
        assertNavigationBackstack(list = listOf(Screen.Feedback(), Screen.Splash()))
    }
}
