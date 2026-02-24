package com.hybris.tlv.ui.screen.feedback

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class FeedbackStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        val store = getStoreFactory().getFeedbackStore(tag = null, message = null)
        assertFalse(actual = store.state.isError)
        assertEquals(expected = "", actual = store.state.feedback)
        assertFalse(actual = store.state.showThanks)
    }

    @Test
    fun sendFeedback() = runUnitTest {
        val store = getStoreFactory().getFeedbackStore(tag = "tag", message = "message")
        store.send(action = FeedbackAction.SendFeedback(message = "feedback"))
        assertTrue(actual = store.state.isError)
        assertEquals(expected = "feedback", actual = store.state.feedback)
        assertTrue(actual = store.state.showThanks)
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Feedback())
        assertNavigation(list = listOf(Screen.Feedback()))
        getStoreFactory().getFeedbackStore(tag = null, message = null).navigateBack()
        assertNavigation(list = emptyList())

        val tag = "tag"
        val message = "message"
        navigate(screen = Screen.Feedback(tag = tag, message = message))
        assertNavigation(list = listOf(Screen.Feedback()))
        getStoreFactory().getFeedbackStore(tag = tag, message = message).navigateBack()
        assertNavigation(list = listOf(Screen.Feedback(), Screen.Splash()))
    }
}
