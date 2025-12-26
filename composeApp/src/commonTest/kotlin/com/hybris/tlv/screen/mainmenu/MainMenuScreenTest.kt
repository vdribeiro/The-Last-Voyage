package com.hybris.tlv.screen.mainmenu

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype

@OptIn(ExperimentalTestApi::class)
internal class MainMenuScreenTest: TestCase() {

    @Test
    fun mainMenuWithoutData() = runUITest {
        val store = storeFactory.getMainMenuStore()
        setScreen { MainMenuScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "website").assertIsDisplayed()
        onNodeWithText(text = "main_menu_screen__credits").assertIsDisplayed()
    }

    @Test
    fun mainMenuWithData() = runUITest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getMainMenuStore()
        setScreen { MainMenuScreen(store = store) }

//        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_NEW_GAME).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_CONTINUE).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SCORES).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT).assertDoesNotExist()

//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_CONTENT_STELLAR_EXPLORER).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_HOST_DEFINITION).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_PLANET_DEFINITION).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_HABITABILITY).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_MECHANICS).assertExists()
    }

    @Test
    fun mainMenuHostDefinitionContent() = runUITest {
//        runBlocking {
//            useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        }
//        val store = getMainMenuStore()
//        setContent {
//            AppTheme {
//                MainMenuScreen(store = store)
//            }
//        }
//        waitForIdle()

//        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_HOST_DEFINITION).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_EXAMPLE).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_EXAMPLE_STELLAR_HOST).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_PROPERTIES).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_PROPERTIES_SIMPLE).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_TYPES).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT_TYPES_STELLAR_HOST).assertExists()
    }

    @Test
    fun mainMenuPlanetDefinitionContent() = runUITest {
//        runBlocking {
//            useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        }
//        val store = getMainMenuStore()
//        setContent {
//            AppTheme {
//                MainMenuScreen(store = store)
//            }
//        }
//        waitForIdle()

//        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_PLANET_DEFINITION).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_EXAMPLE).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_EXAMPLE_PLANET).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_PROPERTIES).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_PROPERTIES_SIMPLE).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_TYPES).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_TYPES_PLANET).assertExists()
    }

    @Test
    fun mainMenuHabitabilityContent() = runUITest {
//        runBlocking {
//            useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        }
//        val store = getMainMenuStore()
//        setContent {
//            AppTheme {
//                MainMenuScreen(store = store)
//            }
//        }
//        waitForIdle()

//        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_HABITABILITY).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_SIMPLE).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_FORMULA).assertExists()
    }
}
