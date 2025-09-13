package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import com.hybris.tlv.storeFactory
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class FeedbackStoreTest {

    private val store: FeedbackStore get() = storeFactory.createFeedbackStore()

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.FEEDBACK)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action send feedback`() = runBlocking {
        val feedbackStore = store
        feedbackStore.send(action = FeedbackAction.SendFeedback(message = "Feedback"))
        // TODO: implement feedback on store
    }
}
