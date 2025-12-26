package com.hybris.tlv.screen.event

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.ship

@OptIn(ExperimentalTestApi::class)
internal class EventScreenTest: TestCase() {

    @Test
    fun eventWithoutData() = runUITest {
        val store = storeFactory.getEventStore(ship = ship)
        setScreen { EventScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithTag(testTag = "event_status_bar").assertIsDisplayed()
        onNodeWithTag(testTag = "event_buttons_bar").assertIsDisplayed()
        onNodeWithTag(testTag = "event_content").assertDoesNotExist()
    }

    @Test
    fun eventWithData() = runUITest {
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getEventStore(ship = ship)
        setScreen { EventScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithTag(testTag = "event_status_bar").assertIsDisplayed()
        onNodeWithTag(testTag = "event_buttons_bar").assertIsDisplayed()
        onNodeWithTag(testTag = "event_content").assertIsDisplayed()
    }
}
