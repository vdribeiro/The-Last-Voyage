package com.hybris.tlv.screen.game

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
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

        onNodeWithTag(testTag = "game_navigation_bar").assertIsDisplayed()

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

        onNodeWithTag(testTag = "game_navigation_bar").assertIsDisplayed()

        onNodeWithTag(testTag = "game_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "game_system_list").assertIsDisplayed()
        onNodeWithTag(testTag = "game_travel_list").assertDoesNotExist()

        onNodeWithTag(testTag = "game_system_list").count(count = 3)

        val navButtons = onAllNodes(matcher = hasAnyAncestor(matcher = hasTestTag(testTag = "game_navigation_bar")) and hasClickAction())

        navButtons[0].performClick()
        onNodeWithTag(testTag = "game_ship_stats").assertIsDisplayed()
        onNodeWithTag(testTag = "game_system_list").assertDoesNotExist()
        onNodeWithTag(testTag = "game_travel_list").assertDoesNotExist()

        onNodeWithText(text = "ship_years_traveled").assertIsDisplayed()
        onNodeWithText(text = "ship_sensor").assertIsDisplayed()
        onNodeWithText(text = "ship_speed").assertIsDisplayed()
        onNodeWithText(text = "ship_integrity").assertIsDisplayed()
        onNodeWithText(text = "ship_fuel").assertIsDisplayed()
        onNodeWithText(text = "ship_materials").assertIsDisplayed()
        onNodeWithText(text = "ship_cryopods").assertIsDisplayed()

        navButtons[1].performClick()
        onNodeWithTag(testTag = "game_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "game_system_list").assertIsDisplayed()
        onNodeWithTag(testTag = "game_travel_list").assertDoesNotExist()

        navButtons[2].performClick()
        onNodeWithTag(testTag = "game_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "game_system_list").assertDoesNotExist()
        onNodeWithTag(testTag = "game_travel_list").assertIsDisplayed()

        onNodeWithTag(testTag = "game_travel_list").count(count = 5)
    }
}
