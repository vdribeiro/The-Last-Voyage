package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class FeedbackStoreTest {

    private val store: FeedbackStore get() = storeFactory.createFeedbackStore()

    @BeforeTest
    fun setup() = runBlocking {
        testCore.sqlDriver.clearDatabase()
        testCore.navigation.navigate(screen = NavigationManager.Screen.Splash)
        testCore.navigation.navigate(screen = NavigationManager.Screen.Feedback)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = NavigationManager.Screen.Feedback, actual = testCore.navigation.stateFlow.value.screen)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.Splash, actual = testCore.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action send feedback`() = runBlocking {
        val feedbackStore = store
        feedbackStore.send(action = FeedbackAction.SendFeedback(message = "Feedback"))
        // TODO: implement feedback on store
    }
}
