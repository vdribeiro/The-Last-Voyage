package com.hybris.tlv.ui.screen.feedback

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.reset
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen

internal class FeedbackStoreTest {

    private val store: FeedbackStore get() = storeFactory.createFeedbackStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Splash))
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Feedback))
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = Screen.Splash, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
