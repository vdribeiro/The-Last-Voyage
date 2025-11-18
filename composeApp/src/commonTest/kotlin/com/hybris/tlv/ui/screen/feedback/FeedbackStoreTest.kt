package com.hybris.tlv.ui.screen.feedback

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.reset
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Route

internal class FeedbackStoreTest {

    private val store: FeedbackStore get() = getStoreFactory().createFeedbackStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(route = Route.Splash))
        getNavigation().navigate(navigationState = NavigationState(route = Route.Feedback))
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
        getNavigation().back()
        assertEquals(expected = Route.Splash, actual = getNavigation().stateFlow.value.route)
    }
}
