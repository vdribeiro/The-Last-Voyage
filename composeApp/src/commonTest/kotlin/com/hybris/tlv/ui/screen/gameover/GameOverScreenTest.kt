package com.hybris.tlv.ui.screen.gameover

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class GameOverScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.sqlDriver.clearDatabase()
    }

    @Test
    fun gameOverWithoutData() = runComposeUiTest {
        val store = storeFactory.createGameOverStore()
        setContent {
            AppTheme {
                GameOverScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = GAME_OVER_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_COLUMN).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_TITLE).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_MESSAGE).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_BUTTON).assertDoesNotExist()
    }

    @Test
    fun gameOverWithData() = runComposeUiTest {
        runBlocking { testCore.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype) }
        val store = storeFactory.createGameOverStore()
        setContent {
            AppTheme {
                GameOverScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = GAME_OVER_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_COLUMN).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_TITLE).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_MESSAGE).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_BUTTON).assertExists().performClick()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_BUTTON).assertExists().performClick()
    }
}
