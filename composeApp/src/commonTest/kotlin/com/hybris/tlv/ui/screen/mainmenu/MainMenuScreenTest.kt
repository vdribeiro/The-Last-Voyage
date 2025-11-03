package com.hybris.tlv.ui.screen.mainmenu

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalTestApi::class)
internal class MainMenuScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testDependency.sqlDriver.clearDatabase()
    }

    @Test
    fun mainMenuWithoutData() = runComposeUiTest {
        val store = storeFactory.createMainMenuStore()
        setContent {
            AppTheme {
                MainMenuScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_NEW_GAME).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_CONTINUE).assertDoesNotExist()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SCORES).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT).assertDoesNotExist()
    }

    @Test
    fun mainMenuWithData() = runComposeUiTest {
        runBlocking {
            testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
            testDependency.useCases.learning.syncLearnings()
        }
        val store = storeFactory.createMainMenuStore()
        setContent {
            AppTheme {
                MainMenuScreen(store = store)
            }
        }
        waitForIdle()

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
    fun mainMenuHostDefinitionContent() = runComposeUiTest {
        runBlocking {
            testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
            testDependency.useCases.learning.syncLearnings()
        }
        val store = storeFactory.createMainMenuStore()
        setContent {
            AppTheme {
                MainMenuScreen(store = store)
            }
        }
        waitForIdle()

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
    fun mainMenuPlanetDefinitionContent() = runComposeUiTest {
        runBlocking {
            testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
            testDependency.useCases.learning.syncLearnings()
        }
        val store = storeFactory.createMainMenuStore()
        setContent {
            AppTheme {
                MainMenuScreen(store = store)
            }
        }
        waitForIdle()

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
    fun mainMenuHabitabilityContent() = runComposeUiTest {
        runBlocking {
            testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
            testDependency.useCases.learning.syncLearnings()
        }
        val store = storeFactory.createMainMenuStore()
        setContent {
            AppTheme {
                MainMenuScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_HABITABILITY).performClick()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_SIMPLE).assertExists()
//        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_FORMULA).assertExists()
    }
}
