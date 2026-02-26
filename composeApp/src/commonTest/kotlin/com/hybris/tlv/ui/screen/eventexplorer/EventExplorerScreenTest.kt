package com.hybris.tlv.ui.screen.eventexplorer

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.count

@OptIn(ExperimentalTestApi::class)
internal class EventExplorerScreenTest: TestCase() {

    @Test
    fun catastropheWithoutData() = runUITest {
        val store = storeFactory.get().getEventExplorerStore()
        setUI { EventExplorerScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "event_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "event_list").assertIsDisplayed()

        onNodeWithTag(testTag = "event_list").count(count = 0)
    }

    @Test
    fun catastropheWithData() = runUITest {
        dependency.get().useCases.event.syncEvents()
        val store = storeFactory.get().getEventExplorerStore()
        setUI { EventExplorerScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "event_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "event_list").assertIsDisplayed()

        onNodeWithTag(testTag = "event_list").count(count = FakeData.events.get().size)
    }
}
