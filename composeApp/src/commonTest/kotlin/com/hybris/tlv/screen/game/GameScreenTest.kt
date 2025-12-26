package com.hybris.tlv.screen.game

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype

@OptIn(ExperimentalTestApi::class)
internal class GameScreenTest: TestCase() {

    @Test
    fun gameWithoutData() = runUITest {
        val store = storeFactory.getGameStore(ship = null)
        setScreen { GameScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithContentDescription(label = "Hull Integrity").assertIsDisplayed()
        onNodeWithContentDescription(label = "Fuel").assertIsDisplayed()
        onNodeWithContentDescription(label = "Materials").assertIsDisplayed()
        onNodeWithContentDescription(label = "Cryopods").assertIsDisplayed()

        onNodeWithText(text = "game_screen__travel").assertIsDisplayed()
        onNodeWithText(text = "game_screen__system").assertIsDisplayed()
        onNodeWithText(text = "game_screen__ship").assertIsDisplayed()

        onNodeWithTag(testTag = "game_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "game_system_list").assertDoesNotExist()
        onNodeWithTag(testTag = "game_travel_list").assertDoesNotExist()
    }

    @Test
    fun gameWithData() = runUITest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameStore(ship = null)
        setScreen { GameScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithContentDescription(label = "Hull Integrity").assertIsDisplayed()
        onNodeWithContentDescription(label = "Fuel").assertIsDisplayed()
        onNodeWithContentDescription(label = "Materials").assertIsDisplayed()
        onNodeWithContentDescription(label = "Cryopods").assertIsDisplayed()

        onNodeWithText(text = "game_screen__travel").assertIsDisplayed()
        onNodeWithText(text = "game_screen__system").assertIsDisplayed()
        onNodeWithText(text = "game_screen__ship").assertIsDisplayed()

        onNodeWithTag(testTag = "game_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "game_system_list").assertIsDisplayed()
        onNodeWithTag(testTag = "game_travel_list").assertDoesNotExist()

        onNodeWithTag(testTag = "game_system_list").count(count = 3)

        onNodeWithText(text = "game_screen__travel").performClick()
        onNodeWithTag(testTag = "game_ship_stats").assertIsDisplayed()
        onNodeWithTag(testTag = "game_system_list").assertDoesNotExist()
        onNodeWithTag(testTag = "game_travel_list").assertDoesNotExist()

//        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP).performClick()
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT).assertExists()
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_YEARS_TRAVELED).assertExists().assertTextContains("ship_years_traveled")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_SENSOR).assertExists().assertTextContains("ship_sensor")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_SPEED).assertExists().assertTextContains("ship_speed")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_INTEGRITY).assertExists().assertTextContains("ship_integrity")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_FUEL).assertExists().assertTextContains("ship_fuel")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_MATERIALS).assertExists().assertTextContains("ship_materials")
//        onNodeWithTag(testTag = GAME_SCREEN_SHIP_CONTENT_CRYOPODS).assertExists().assertTextContains("ship_cryopods")
//
//        onNodeWithTag(testTag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).performClick()
//        onNodeWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT).assertExists()
//        onAllNodesWithTag(testTag = GAME_SCREEN_TRAVEL_CONTENT_STELLAR_HOST).onLast().assertIsDisplayed()
    }
}
