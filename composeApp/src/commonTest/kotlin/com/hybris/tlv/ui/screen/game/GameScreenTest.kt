package com.hybris.tlv.ui.screen.game

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
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class GameScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun gameWithoutData() = runComposeUiTest {
        val store = storeFactory.createGameStore()
        setContent {
            GameScreen(store = store)
        }

        onNodeWithTag(testTag = GAME_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_STATUS_BAR).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertDoesNotExist()
    }

    @Test
    fun gameWithData() = runComposeUiTest {
        runBlocking {
            mock.useCases.space.prepopulateStellarHosts()
            mock.useCases.space.prepopulatePlanets()
            mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        }
        val store = storeFactory.createGameStore()
        setContent {
            GameScreen(store = store)
        }

        onNodeWithTag(testTag = GAME_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_STATUS_BAR).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertDoesNotExist()
    }

    @Test
    fun gameTutorial() = runComposeUiTest {
        runBlocking { mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype) }
        val store = storeFactory.createGameStore(
            stateBuilder = GameStateBuilder(
                tutorial = true
            )
        )
        setContent {
            GameScreen(store = store)
        }

        onNodeWithTag(testTag = GAME_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_PROGRESS_INDICATOR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertExists()
    }
}
