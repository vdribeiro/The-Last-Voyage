package com.hybris.tlv.ui.screen.gameover

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype

@OptIn(ExperimentalTestApi::class)
internal class GameOverScreenTest: TestCase() {

    @Test
    fun gameOverWithoutData() = runUITest {
        val store = storeFactory.getGameOverStore()
        setScreen { GameOverScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "game_over_screen__score").assertDoesNotExist()
        onNodeWithText(text = "game_over_screen__end").assertDoesNotExist()

        onNodeWithTag(testTag = "game_over_message_content").assertIsDisplayed()
        onNodeWithTag(testTag = "game_over_score_content").assertDoesNotExist()
        onNodeWithTag(testTag = "game_over_score").assertDoesNotExist()
    }

    @Test
    fun gameOverWithData() = runUITest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameOverStore()
        setScreen { GameOverScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "game_over_screen__score").assertIsDisplayed()
        onNodeWithText(text = "game_over_screen__end").assertDoesNotExist()

        onNodeWithTag(testTag = "game_over_message_content").assertIsDisplayed()
        onNodeWithTag(testTag = "game_over_score_content").assertDoesNotExist()
        onNodeWithTag(testTag = "game_over_score").assertDoesNotExist()

        onNodeWithText(text = "game_over_screen__score").performClick()

        onNodeWithText(text = "game_over_screen__score").assertDoesNotExist()
        onNodeWithText(text = "game_over_screen__end").assertIsDisplayed()

        onNodeWithTag(testTag = "game_over_message_content").assertDoesNotExist()
        onNodeWithTag(testTag = "game_over_score_content").assertIsDisplayed()
        onNodeWithTag(testTag = "game_over_score").assertIsDisplayed()

        onNodeWithText(text = "game_over_screen__end").performClick()
    }
}
