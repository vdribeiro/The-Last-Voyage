package com.hybris.tlv.ui.screen.game

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class GameScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testDependency.sqlDriver.clearDatabase()
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
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertDoesNotExist()
    }

    // TODO
    @Test
    fun gameWithData() = runComposeUiTest {
        runBlocking {
            testDependency.useCases.space.syncStellarHosts()
            testDependency.useCases.space.syncPlanets()
            testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
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
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertDoesNotExist()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertDoesNotExist()

        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT_STELLAR_HOST).assertExists()
        onAllNodesWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT_PLANET).onLast().assertIsDisplayed()

        //onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).performClick()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertExists()
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_YEARS_TRAVELED).assertExists().assertTextContains("ship_years_traveled")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_SENSOR).assertExists().assertTextContains("ship_sensor")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_SPEED).assertExists().assertTextContains("ship_speed")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_INTEGRITY).assertExists().assertTextContains("ship_integrity")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_FUEL).assertExists().assertTextContains("ship_fuel")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_MATERIALS).assertExists().assertTextContains("ship_materials")
        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_CRYOPODS).assertExists().assertTextContains("ship_cryopods")

        //onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).performClick()
        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertExists()
        onAllNodesWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT_STELLAR_HOST).onLast().assertIsDisplayed()
    }
}
