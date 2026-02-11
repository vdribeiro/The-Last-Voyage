package com.hybris.tlv.ui.screen.cheat

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.count

@OptIn(ExperimentalTestApi::class)
internal class CheatScreenTest: TestCase() {

    @Test
    fun cheat() = runUITest {
        val store = getStoreFactory().getCheatStore()
        setUI { CheatScreen(store = store) }

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
        onNodeWithTag(testTag = "cheat_list").count(count = 5)

        assertFalse(actual = store.state.integrity)
        onNodeWithText(text = "cheats_screen__integrity").performClick()
        assertTrue(actual = store.state.integrity)
        onNodeWithText(text = "cheats_screen__integrity").performClick()
        assertFalse(actual = store.state.integrity)

        assertFalse(actual = store.state.sensorRange)
        onNodeWithText(text = "cheats_screen__sensor_range").performClick()
        assertTrue(actual = store.state.sensorRange)
        onNodeWithText(text = "cheats_screen__sensor_range").performClick()
        assertFalse(actual = store.state.sensorRange)

        assertFalse(actual = store.state.fuel)
        onNodeWithText(text = "cheats_screen__fuel").performClick()
        assertTrue(actual = store.state.fuel)
        onNodeWithText(text = "cheats_screen__fuel").performClick()
        assertFalse(actual = store.state.fuel)

        assertFalse(actual = store.state.materials)
        onNodeWithText(text = "cheats_screen__materials").performClick()
        assertTrue(actual = store.state.materials)
        onNodeWithText(text = "cheats_screen__materials").performClick()
        assertFalse(actual = store.state.materials)

        assertFalse(actual = store.state.cryopods)
        onNodeWithText(text = "cheats_screen__cryopods").performClick()
        assertTrue(actual = store.state.cryopods)
        onNodeWithText(text = "cheats_screen__cryopods").performClick()
        assertFalse(actual = store.state.cryopods)
    }
}
