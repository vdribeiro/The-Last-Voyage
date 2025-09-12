package com.hybris.tlv.ui.screen.event

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.events
import com.hybris.tlv.gameSession
import com.hybris.tlv.mock
import com.hybris.tlv.storeFactory
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class EventScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun eventNull() = runComposeUiTest {
        val store = storeFactory.createEventStore()
        setContent {
            EventScreen(store = store)
        }

        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_DESCRIPTION).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS_ITEM).assertDoesNotExist()
    }

    @Test
    fun eventEmpty() = runComposeUiTest {
        val store = storeFactory.createEventStore(
            state = EventState(
                gameSession = gameSession,
                events = emptyList()
            )
        )
        setContent {
            EventScreen(store = store)
        }

        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_DESCRIPTION).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS).assertDoesNotExist()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS_ITEM).assertDoesNotExist()
    }

    @Test
    fun eventList() = runComposeUiTest {
        val store = storeFactory.createEventStore(
            state = EventState(
                gameSession = gameSession,
                events = events,
                event = events.first(),
                children = events
            )
        )
        setContent {
            EventScreen(store = store)
        }

        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_DESCRIPTION).assertExists()
        onNodeWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS).assertExists()
        onAllNodesWithTag(testTag = EVENT_SCREEN_COLUMN_EVENT_BUTTONS_ITEM)
            .assertCountEquals(expectedSize = events.size)
    }
}
