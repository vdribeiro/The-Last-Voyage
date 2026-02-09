package com.hybris.tlv.ui.screen.mainmenu

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

@OptIn(ExperimentalTestApi::class)
internal class MainMenuScreenTest: TestCase() {

    @Test
    fun mainMenuWithoutData() = runUITest {
        val store = getStoreFactory().getMainMenuStore()
        setUI { MainMenuScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "website").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__credits").assertIsDisplayed()

        onNodeWithText(text = "main_menu_screen__new_game").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__continue").assertDoesNotExist()
        onNodeWithText(text = "main_menu_screen__stellar_explorer").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__scores").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__achievements").assertIsDisplayed()
    }

    @Test
    fun mainMenuWithData() = runUITest {
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = getStoreFactory().getMainMenuStore()
        setUI { MainMenuScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "website").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__credits").assertIsDisplayed()

        onNodeWithText(text = "main_menu_screen__new_game").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__continue").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__stellar_explorer").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__scores").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__achievements").assertIsDisplayed()
    }
}
