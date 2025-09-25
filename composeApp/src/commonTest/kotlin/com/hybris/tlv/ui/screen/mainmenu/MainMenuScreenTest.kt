package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.mock
import com.hybris.tlv.storeFactory
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class MainMenuScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun mainMenuWithoutData() = runComposeUiTest {
        val store = storeFactory.createMainMenuStore()
        setContent {
            AppTheme {
                MainMenuScreen(store = store)
            }
        }

        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_WEBSITE).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_CREDITS).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_SUPPORT).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_NEW_GAME).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_CONTINUE).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SCORES).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SOON).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT).assertDoesNotExist()
    }

    @Test
    fun mainMenuWithData() = runComposeUiTest {
        runBlocking {
            mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
            mock.useCases.learning.prepopulateLearnings()
        }
        val store = storeFactory.createMainMenuStore()
        setContent {
            AppTheme {
                MainMenuScreen(store = store)
            }
        }

        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_WEBSITE).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_CREDITS).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_SUPPORT).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_NEW_GAME).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_CONTINUE).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SCORES).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SOON).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT).assertDoesNotExist()

        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN).performClick()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_STELLAR_EXPLORER).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_HOST_DEFINITION).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_PLANET_DEFINITION).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_HABITABILITY).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT_MECHANICS).assertExists()

        //learnings.forEachIndexed { index, _ ->
        //    onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT).onChildren()[index].assertExists()
        //}

        //onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_FORMULA).performScrollTo().assertExists()
        //onAllNodesWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_SIMPLE)
        //    .assertCountEquals(expectedSize = learnings.size)
        //onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_FORMULA).assertExists().assertTextEquals("formula")
    }
}
