package com.hybris.tlv.screen.newgame

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class NewGameScreenTest: TestCase() {

    @Test
    fun newGameWithoutData() = runUITest {
        val store = storeFactory.getNewGameStore()
        setScreen { NewGameScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "new_game_screen__continue").assertDoesNotExist()
        onNodeWithText(text = "new_game_screen__start").assertDoesNotExist()

        onNodeWithText(text = "ship_sensor").assertDoesNotExist()
        onNodeWithText(text = "ship_fuel").assertDoesNotExist()
        onNodeWithText(text = "ship_materials").assertDoesNotExist()
        onNodeWithText(text = "ship_cryopods").assertDoesNotExist()
        onNodeWithText(text = "new_game_screen__engine_select").assertDoesNotExist()
        onNodeWithTag(testTag = "new_game_content").assertDoesNotExist()
    }

    @Test
    fun newGameWithData() = runUITest {
        useCases.ship.syncEngines()
        val store = storeFactory.getNewGameStore()
        setScreen { NewGameScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "new_game_screen__continue").assertIsDisplayed()
        onNodeWithText(text = "new_game_screen__start").assertDoesNotExist()

        onNodeWithText(text = "ship_sensor").assertIsDisplayed()
        onNodeWithText(text = "ship_fuel").assertIsDisplayed()
        onNodeWithText(text = "ship_materials").assertIsDisplayed()
        onNodeWithText(text = "ship_cryopods").assertIsDisplayed()
        onNodeWithText(text = "new_game_screen__engine_select").assertIsDisplayed()
        onNodeWithTag(testTag = "new_game_content").assertDoesNotExist()

        onNodeWithText(text = "new_game_screen__continue").performClick()

        onNodeWithText(text = "new_game_screen__continue").assertDoesNotExist()
        onNodeWithText(text = "new_game_screen__start").assertIsDisplayed()

        onNodeWithText(text = "ship_sensor").assertDoesNotExist()
        onNodeWithText(text = "ship_fuel").assertDoesNotExist()
        onNodeWithText(text = "ship_materials").assertDoesNotExist()
        onNodeWithText(text = "ship_cryopods").assertDoesNotExist()
        onNodeWithText(text = "new_game_screen__engine_select").assertDoesNotExist()
        onNodeWithTag(testTag = "new_game_content").assertIsDisplayed()
    }
}
