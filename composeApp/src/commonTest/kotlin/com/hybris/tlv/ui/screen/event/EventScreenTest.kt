package com.hybris.tlv.ui.screen.event

import kotlin.test.Test
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class EventScreenTest: TestCase() {

    @Test
    fun eventWithoutData() = runUITest {
        val store = storeFactory.get().getEventStore(ship = FakeData.ship.get())
        setUI { EventScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithContentDescription(label = "Hull Integrity").assertIsDisplayed()
        onNodeWithContentDescription(label = "Fuel").assertIsDisplayed()
        onNodeWithContentDescription(label = "Materials").assertIsDisplayed()
        onNodeWithContentDescription(label = "Cryopods").assertIsDisplayed()

        onNodeWithTag(testTag = "event_buttons_bar").assertIsDisplayed()
        onNodeWithTag(testTag = "event_content").assertDoesNotExist()
    }

    @Test
    fun eventWithData() = runUITest {
        dependency.get().useCases.event.prepopulateEvents()
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = storeFactory.get().getEventStore(ship = FakeData.ship.get())
        setUI { EventScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithContentDescription(label = "Hull Integrity").assertIsDisplayed()
        onNodeWithContentDescription(label = "Fuel").assertIsDisplayed()
        onNodeWithContentDescription(label = "Materials").assertIsDisplayed()
        onNodeWithContentDescription(label = "Cryopods").assertIsDisplayed()

        onNodeWithTag(testTag = "event_buttons_bar").assertIsDisplayed()
        onNodeWithTag(testTag = "event_content").assertIsDisplayed()
        onNodeWithTag(testTag = "event_buttons_bar").onChildAt(index = 0).performClick()
    }
}
