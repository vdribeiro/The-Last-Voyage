package com.hybris.tlv.ui.screen.feedback

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class FeedbackStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getFeedbackStore(tag = null, message = null)
        assertFalse(actual = store.state.isError)
        assertEquals(expected = "", actual = store.state.feedback)
        assertFalse(actual = store.state.showThanks)
    }

    @Test
    fun sendFeedback() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getFeedbackStore(tag = "tag", message = "message")
        store.send(action = FeedbackAction.SendFeedback(message = "feedback"))
        assertTrue(actual = store.state.isError)
        assertEquals(expected = "feedback", actual = store.state.feedback)
        assertTrue(actual = store.state.showThanks)
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Feedback())
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
        TestCase.storeFactory.getFeedbackStore(tag = null, message = null).back()
        TestCase.assertNavigation(list = emptyList())

        val tag = "tag"
        val message = "message"
        TestCase.navigate(screen = Screen.Feedback(tag = tag, message = message))
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
        TestCase.storeFactory.getFeedbackStore(tag = tag, message = message).back()
        TestCase.assertNavigation(list = listOf(Screen.Feedback(), Screen.Splash()))
    }
}
