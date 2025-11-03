package com.hybris.tlv.ui.screen.event

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalTestApi::class)
internal class EventScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testDependency.sqlDriver.clearDatabase()
    }

    @Test
    fun eventWithoutData() = runComposeUiTest {
        val store = storeFactory.createEventStore()
        setContent {
            AppTheme {
                EventScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
//        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
    }

    @Test
    fun eventWithData() = runComposeUiTest {
        runBlocking {
            testDependency.useCases.event.syncEvents()
            testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        }
        val store = storeFactory.createEventStore()
        setContent {
            AppTheme {
                EventScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
//        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
        // TODO
    }
}
