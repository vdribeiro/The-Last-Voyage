package com.hybris.tlv.ui.screen.help

import kotlin.test.Test
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.hybris.tlv.test.TestCase

internal class HelpScreenTest: TestCase() {

    @Test
    fun help() = runUITest {
        val store = storeFactory.get().getHelpStore()
        setUI { HelpScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithTag(testTag = "help_main").assertIsDisplayed()
        onNodeWithTag(testTag = "help_navigation").assertDoesNotExist()
        onNodeWithTag(testTag = "help_control_panel").assertDoesNotExist()
        onNodeWithTag(testTag = "help_host_definition").assertDoesNotExist()
        onNodeWithTag(testTag = "help_host_type").assertDoesNotExist()
        onNodeWithTag(testTag = "help_planet_definition").assertDoesNotExist()
        onNodeWithTag(testTag = "help_planet_type").assertDoesNotExist()
        onNodeWithTag(testTag = "help_habitability").assertDoesNotExist()
        onNodeWithTag(testTag = "help_score").assertDoesNotExist()
    }
}
