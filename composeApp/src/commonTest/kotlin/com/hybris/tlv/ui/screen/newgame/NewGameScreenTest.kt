package com.hybris.tlv.screen.newgame

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.getNewGameStore
import com.hybris.tlv.reset
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.useCases

@OptIn(ExperimentalTestApi::class)
internal class NewGameScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun newGameWithoutData() = runComposeUiTest {
        val store = getNewGameStore()
        setContent {
            AppTheme {
                NewGameScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = NEW_GAME_SCREEN).assertExists()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS_TEXT).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE_DESCRIPTION).assertDoesNotExist()
    }

    @Test
    fun newGameWithData() = runComposeUiTest {
        runBlocking {
            useCases.catastrophe.syncCatastrophes()
        }
        val store = getNewGameStore()
        setContent {
            AppTheme {
                NewGameScreen(store = store)
            }
        }
        waitForIdle()

        // TODO
//        onNodeWithTag(testTag = NEW_GAME_SCREEN).assertExists()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT).assertExists()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS_TEXT).assertExists()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS).assertExists()
        //onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_BUTTON).assertExists()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE_DESCRIPTION).assertDoesNotExist()
        //onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_BUTTON).assertDoesNotExist()

        //onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_BUTTON).performClick()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS_TEXT).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS).assertDoesNotExist()
        //onNodeWithTag(testTag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_BUTTON).assertDoesNotExist()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT).assertExists()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE).assertExists()
//        onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_CATASTROPHE_DESCRIPTION).assertExists()
        //onNodeWithTag(testTag = NEW_GAME_SCREEN_START_CONTENT_BUTTON).assertExists()
    }
}
