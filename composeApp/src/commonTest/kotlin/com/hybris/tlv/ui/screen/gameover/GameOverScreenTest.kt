package com.hybris.tlv.ui.screen.gameover

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.mock
import com.hybris.tlv.storeFactory
import com.hybris.tlv.ui.screen.feedback.FEEDBACK_SCREEN
import com.hybris.tlv.ui.screen.feedback.FEEDBACK_SCREEN_BUTTON
import com.hybris.tlv.ui.screen.feedback.FEEDBACK_SCREEN_COLUMN
import com.hybris.tlv.ui.screen.feedback.FEEDBACK_SCREEN_DESCRIPTION
import com.hybris.tlv.ui.screen.feedback.FEEDBACK_SCREEN_ICON
import com.hybris.tlv.ui.screen.feedback.FEEDBACK_SCREEN_INPUT
import com.hybris.tlv.ui.screen.feedback.FEEDBACK_SCREEN_THANKS
import com.hybris.tlv.ui.screen.feedback.FEEDBACK_SCREEN_TITLE
import com.hybris.tlv.ui.screen.feedback.FeedbackScreen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.screen.game.GAME_SCREEN
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_PROGRESS_INDICATOR
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_STATUS_BAR
import com.hybris.tlv.ui.screen.game.GameScreen
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class GameOverScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun gameOverWithoutData() = runComposeUiTest {
        val store = storeFactory.createGameOverStore()
        setContent {
            GameOverScreen(store = store)
        }

        onNodeWithTag(testTag = GAME_OVER_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_COLUMN).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_TITLE).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_MESSAGE).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_BUTTON).assertDoesNotExist()
    }

    @Test
    fun gameOverWithData() = runComposeUiTest {
        runBlocking { mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype) }
        val store = storeFactory.createGameOverStore()
        setContent {
            GameOverScreen(store = store)
        }

        onNodeWithTag(testTag = GAME_OVER_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_COLUMN).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_TITLE).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_MESSAGE).assertExists()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_BUTTON).assertExists().performClick()
        onNodeWithTag(testTag = GAME_OVER_SCREEN_SCORE).assertExists()
    }
}
