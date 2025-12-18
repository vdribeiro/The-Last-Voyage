package com.hybris.tlv.screen.gameover

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getGameOverStore
import com.hybris.tlv.reset
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.useCases

@OptIn(ExperimentalTestApi::class)
internal class GameOverScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun gameOverWithoutData() = runComposeUiTest {
        val store = getGameOverStore()
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
        runBlocking { useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype) }
        val store = getGameOverStore()
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
