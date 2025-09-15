package com.hybris.tlv.ui.screen.game

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
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
internal class GameScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun gameWithoutData() = runComposeUiTest {
        val store = storeFactory.createGameStore()
        setContent {
            AppTheme(testing = true) {
                GameScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = GAME_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).assertExists().assertTextEquals("game_screen__ship")
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM).assertExists().assertTextEquals("game_screen__system")
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).assertExists().assertTextEquals("game_screen__travel")
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
            AppTheme(testing = true) {
                GameScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = GAME_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).assertExists().assertTextEquals("game_screen__ship")
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM).assertExists().assertTextEquals("game_screen__system")
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).assertExists().assertTextEquals("game_screen__travel")
        onNodeWithTag(testTag = GAME_SCREEN_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT, useUnmergedTree = true).assertExists()
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
            AppTheme(testing = true) {
                GameScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = GAME_SCREEN).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).assertExists().assertTextEquals("game_screen__ship")
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM).assertExists().assertTextEquals("game_screen__system")
        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).assertExists().assertTextEquals("game_screen__travel")
        onNodeWithTag(testTag = GAME_SCREEN_PROGRESS_INDICATOR).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertExists()
    }
}
