package com.hybris.tlv.screen.cheat

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class CheatScreenTest: TestCase() {

    @Test
    fun cheat() = runUITest {
        val store = storeFactory.getCheatStore()
        setScreen { CheatScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_feedback").assertDoesNotExist()

        onNodeWithText(text = "cheats_screen__title").assertIsDisplayed()
        onNodeWithText(text = "cheats_screen__integrity").assertIsDisplayed()
        onNodeWithText(text = "cheats_screen__sensor_range").assertIsDisplayed()
        onNodeWithText(text = "cheats_screen__fuel").assertIsDisplayed()
        onNodeWithText(text = "cheats_screen__materials").assertIsDisplayed()
        onNodeWithText(text = "cheats_screen__cryopods").assertIsDisplayed()
        onNodeWithTag(testTag = "cheat_list").assertIsDisplayed()
        onNodeWithTag(testTag = "cheat_list")
            .onChildren()
            .assertCountEquals(expectedSize = 5)

        assertEquals(expected = false, actual = store.state.integrity)
        onNodeWithText(text = "cheats_screen__integrity").performClick()
        assertEquals(expected = true, actual = store.state.integrity)
        onNodeWithText(text = "cheats_screen__integrity").performClick()
        assertEquals(expected = false, actual = store.state.integrity)

        assertEquals(expected = false, actual = store.state.sensorRange)
        onNodeWithText(text = "cheats_screen__sensor_range").performClick()
        assertEquals(expected = true, actual = store.state.sensorRange)
        onNodeWithText(text = "cheats_screen__sensor_range").performClick()
        assertEquals(expected = false, actual = store.state.sensorRange)

        assertEquals(expected = false, actual = store.state.fuel)
        onNodeWithText(text = "cheats_screen__fuel").performClick()
        assertEquals(expected = true, actual = store.state.fuel)
        onNodeWithText(text = "cheats_screen__fuel").performClick()
        assertEquals(expected = false, actual = store.state.fuel)

        assertEquals(expected = false, actual = store.state.materials)
        onNodeWithText(text = "cheats_screen__materials").performClick()
        assertEquals(expected = true, actual = store.state.materials)
        onNodeWithText(text = "cheats_screen__materials").performClick()
        assertEquals(expected = false, actual = store.state.materials)

        assertEquals(expected = false, actual = store.state.cryopods)
        onNodeWithText(text = "cheats_screen__cryopods").performClick()
        assertEquals(expected = true, actual = store.state.cryopods)
        onNodeWithText(text = "cheats_screen__cryopods").performClick()
        assertEquals(expected = false, actual = store.state.cryopods)
    }
}
