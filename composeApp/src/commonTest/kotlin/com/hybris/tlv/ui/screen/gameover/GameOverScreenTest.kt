package com.hybris.tlv.ui.screen.gameover

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
internal class GameOverScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testDependency.sqlDriver.clearDatabase()
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

//        onNodeWithTag(testTag = GAME_OVER_SCREEN).assertExists()
//        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertDoesNotExist()
    }

    @Test
    fun gameOverWithData() = runComposeUiTest {
        runBlocking { testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype) }
        val store = storeFactory.createGameOverStore()
        setContent {
            AppTheme {
                GameOverScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = GAME_OVER_SCREEN).assertExists()
//        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertDoesNotExist()
        //onNodeWithTag(testTag = GAME_OVER_SCREEN_BUTTON).assertExists().performClick()
//        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertExists()
        //onNodeWithTag(testTag = GAME_OVER_SCREEN_BUTTON).assertExists().performClick()
        // TODO
    }
}
