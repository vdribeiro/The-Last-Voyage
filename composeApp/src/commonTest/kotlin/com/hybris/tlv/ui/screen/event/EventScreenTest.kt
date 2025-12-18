package com.hybris.tlv.screen.event

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getEventStore
import com.hybris.tlv.reset
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.useCases

@OptIn(ExperimentalTestApi::class)
internal class EventScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun eventWithoutData() = runComposeUiTest {
        val store = getEventStore()
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
            useCases.event.syncEvents()
            useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        }
        val store = getEventStore()
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
