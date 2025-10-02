package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.Screen
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class FeedbackStoreTest {

    private val store: FeedbackStore get() = storeFactory.createFeedbackStore()

    @BeforeTest
    fun setup() = runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.navigation.navigate(screen = Screen.Splash)
        testDependency.navigation.navigate(screen = Screen.Feedback)
    }

    @Test
    fun `send action back`() = runBlocking {
        store
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = Screen.Splash, actual = testDependency.navigation.stateFlow.value.screen)
    }
}
