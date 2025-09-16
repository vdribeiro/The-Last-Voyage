package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
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
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_TOP_BAR).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_TOP_BAR_FEEDBACK).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_WEBSITE).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_CREDITS).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_SUPPORT).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_PROGRESS_INDICATOR).assertDoesNotExist()
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
        val store = storeFactory.createMainMenuStore(
            stateBuilder = MainMenuStateBuilder(
                currentContent = Content.HABITABILITY
            )
        )
        setContent {
            AppTheme {
                MainMenuScreen(store = store)
            }
        }

        onNodeWithTag(testTag = MAIN_MENU_SCREEN).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_TOP_BAR).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_TOP_BAR_FEEDBACK).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR).assertExists()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_WEBSITE).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_CREDITS).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_BOTTOM_BAR_SUPPORT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_LEARN_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HOST_DEFINITION_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT).assertExists()
        // TODO
        //onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_SIMPLE).performScrollTo().assertExists()
        //onAllNodesWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_SIMPLE)
        //    .assertCountEquals(expectedSize = learnings.size)
        //onNodeWithTag(testTag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_FORMULA).assertExists().assertTextEquals("formula")
    }
}
