package com.hybris.tlv.ui.screen.event

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

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

        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_DESCRIPTION).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS_ITEM).assertDoesNotExist()
    }

    @Test
    fun eventWithData() = runComposeUiTest {
        runBlocking {
            testDependency.useCases.event.prepopulateEvents()
            testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        }
        val store = storeFactory.createEventStore()
        setContent {
            AppTheme {
                EventScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_DESCRIPTION).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS_ITEM).assertExists().performClick()
    }
}
