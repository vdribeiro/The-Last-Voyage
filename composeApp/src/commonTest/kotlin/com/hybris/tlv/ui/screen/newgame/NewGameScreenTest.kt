package com.hybris.tlv.ui.screen.newgame

import kotlin.test.Test
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.TestCase

internal class NewGameScreenTest: TestCase() {

    @Test
    fun newGameWithoutData() = runUITest {
        val store = storeFactory.get().getNewGameStore()
        setUI { NewGameScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "new_game_screen__start").assertDoesNotExist()

        onNodeWithText(text = "ship_sensor").assertIsDisplayed()
        onNodeWithText(text = "ship_fuel").assertIsDisplayed()
        onNodeWithText(text = "ship_materials").assertIsDisplayed()
        onNodeWithText(text = "ship_cryopods").assertIsDisplayed()
        onNodeWithText(text = "new_game_screen__engine_select").assertDoesNotExist()
    }

    @Test
    fun newGameWithData() = runUITest {
        dependency.get().useCases.ship.syncEngines()
        val store = storeFactory.get().getNewGameStore()
        setUI { NewGameScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "new_game_screen__start").assertIsDisplayed()

        onNodeWithText(text = "ship_sensor").assertIsDisplayed()
        onNodeWithText(text = "ship_fuel").assertIsDisplayed()
        onNodeWithText(text = "ship_materials").assertIsDisplayed()
        onNodeWithText(text = "ship_cryopods").assertIsDisplayed()
        onNodeWithText(text = "new_game_screen__engine_select").assertIsDisplayed()

        onNodeWithText(text = "new_game_screen__start").performClick()
    }
}
