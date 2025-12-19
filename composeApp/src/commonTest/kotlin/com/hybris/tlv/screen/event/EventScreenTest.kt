package com.hybris.tlv.screen.event

import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.ship

@OptIn(ExperimentalTestApi::class)
internal class EventScreenTest: TestCase() {

    @Test
    fun eventWithoutData() = runUITest {
        val store = storeFactory.getEventStore(ship = ship)
        setScreen { EventScreen(store = store) }

//        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
//        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
    }

    @Test
    fun eventWithData() = runUITest {
        runBlocking {
            useCases.event.syncEvents()
            useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        }
        val store = storeFactory.getEventStore(ship = ship)
        setScreen { EventScreen(store = store) }

//        onNodeWithTag(testTag = EVENT_SCREEN).assertExists()
//        onNodeWithTag(testTag = EVENT_SCREEN_STATUS_BAR).assertExists()
        // TODO
    }
}
