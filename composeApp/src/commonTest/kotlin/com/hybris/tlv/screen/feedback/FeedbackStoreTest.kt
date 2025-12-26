package com.hybris.tlv.screen.feedback

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class FeedbackStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        val store = storeFactory.getFeedbackStore(tag = null, message = null)
        assertFalse(actual = store.state.isError)
        assertEquals(expected = "", actual = store.state.feedback)
        assertFalse(actual = store.state.showThanks)
    }

    @Test
    fun sendFeedback() = runUnitTest {
        val store = storeFactory.getFeedbackStore(tag = "tag", message = "message")
        store.send(action = FeedbackAction.SendFeedback(message = "feedback"))
        assertTrue(actual = store.state.isError)
        assertEquals(expected = "feedback", actual = store.state.feedback)
        assertTrue(actual = store.state.showThanks)
    }

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
