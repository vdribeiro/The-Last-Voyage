package com.hybris.tlv.ui.screen.newgame

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class NewGameScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.clearDatabase()
    }

    @Test
    fun newGameWithoutData() = runComposeUiTest {
        val store = storeFactory.createNewGameStore()
        setContent {
            AppTheme {
                NewGameScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = NEW_GAME_SCREEN).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS_TEXT).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_BUTTON).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE_DESCRIPTION).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_BUTTON).assertDoesNotExist()
    }

    @Test
    fun newGameWithData() = runComposeUiTest {
        runBlocking {
            testCore.useCases.catastrophe.prepopulateCatastrophes()
        }
        val store = storeFactory.createNewGameStore()
        setContent {
            AppTheme {
                NewGameScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = NEW_GAME_SCREEN).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS_TEXT).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_BUTTON).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE_DESCRIPTION).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_BUTTON).assertDoesNotExist()

        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_BUTTON).performClick()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS_TEXT).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_BUTTON).assertDoesNotExist()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE_DESCRIPTION).assertExists()
        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_BUTTON).assertExists()
    }
}
