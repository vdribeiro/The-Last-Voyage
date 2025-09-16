package com.hybris.tlv.ui.screen.game

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.mock
import com.hybris.tlv.stellarHosts
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
            AppTheme {
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
            AppTheme {
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
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertDoesNotExist()

        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT_STELLAR_HOST).assertExists()
        onAllNodesWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT_PLANET).onLast().assertIsDisplayed()

        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).performClick()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_YEARS_TRAVELED).assertExists().assertTextContains("ship_years_traveled")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_SENSOR).assertExists().assertTextContains("ship_sensor")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_SPEED).assertExists().assertTextContains("ship_speed")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_INTEGRITY).assertExists().assertTextContains("ship_integrity")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_FUEL).assertExists().assertTextContains("ship_fuel")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_MATERIALS).assertExists().assertTextContains("ship_materials")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_CRYOPODS).assertExists().assertTextContains("ship_cryopods")

        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).performClick()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertExists()
        onAllNodesWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT_STELLAR_HOST).onLast().assertIsDisplayed()
    }

    @Test
    fun gameTutorial() = runComposeUiTest {
        // TODO
    }
}
