package com.hybris.tlv.ui.screen.game

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getGameStore
import com.hybris.tlv.reset
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.useCases

@OptIn(ExperimentalTestApi::class)
internal class GameScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun gameWithoutData() = runComposeUiTest {
        val store = getGameStore()
        setContent {
            AppTheme {
                GameScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = GAME_SCREEN).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_STATUS_BAR).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertDoesNotExist()
    }

    // TODO
    @Test
    fun gameWithData() = runComposeUiTest {
        runBlocking {
            useCases.space.syncStellarHosts()
            useCases.space.syncPlanets()
            useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        }
        val store = getGameStore()
        setContent {
            AppTheme {
                GameScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = GAME_SCREEN).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_STATUS_BAR).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertDoesNotExist()
//        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertDoesNotExist()

//        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT_STELLAR_HOST).assertExists()
//        onAllNodesWithTag(testTag = GAME_SCREEN_SYSTEM_CONTENT_PLANET).onLast().assertIsDisplayed()

        //onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).performClick()
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_YEARS_TRAVELED).assertExists().assertTextContains("ship_years_traveled")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_SENSOR).assertExists().assertTextContains("ship_sensor")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_SPEED).assertExists().assertTextContains("ship_speed")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_INTEGRITY).assertExists().assertTextContains("ship_integrity")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_FUEL).assertExists().assertTextContains("ship_fuel")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_MATERIALS).assertExists().assertTextContains("ship_materials")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_CRYOPODS).assertExists().assertTextContains("ship_cryopods")

        //onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).performClick()
//        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertExists()
//        onAllNodesWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT_STELLAR_HOST).onLast().assertIsDisplayed()
    }
}
