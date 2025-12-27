package com.hybris.tlv.screen.tutorial

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class TutorialScreenTest: TestCase() {

    @Test
    fun tutorial() = runUITest {
        val store = storeFactory.getTutorialStore(newGame = false)
        setScreen { TutorialScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithContentDescription(label = "Hull Integrity").assertIsDisplayed()
        onNodeWithContentDescription(label = "Fuel").assertIsDisplayed()
        onNodeWithContentDescription(label = "Materials").assertIsDisplayed()
        onNodeWithContentDescription(label = "Cryopods").assertIsDisplayed()

        onNodeWithTag(testTag = "tutorial_navigation_bar").assertIsDisplayed()
        val navButtons = onAllNodes(matcher = hasAnyAncestor(matcher = hasTestTag(testTag = "tutorial_navigation_bar")) and hasClickAction())

        onNodeWithText(text = "tutorial_screen__mechanics_welcome_start").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_skip").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_attributes_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_next").assertDoesNotExist()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_description").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_travel_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_system_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_description").assertDoesNotExist()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome_start").performClick()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome_start").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_skip").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_next").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_attributes_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_next").assertDoesNotExist()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_title").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_description").assertIsDisplayed()
        onNodeWithTag(testTag = "tutorial_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_travel_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_system_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_description").assertDoesNotExist()

        navButtons[0].performClick()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome_start").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_skip").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_attributes_next").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_next").assertDoesNotExist()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_ship_stats").assertIsDisplayed()
        onNodeWithTag(testTag = "tutorial_travel_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_system_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_description").assertDoesNotExist()

        navButtons[2].performClick()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome_start").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_skip").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_attributes_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_next").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_system_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_next").assertDoesNotExist()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_travel_list").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_description").assertIsDisplayed()
        onNodeWithTag(testTag = "tutorial_system_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_description").assertDoesNotExist()

        navButtons[1].performClick()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome_start").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_skip").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_attributes_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_next").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_next").assertDoesNotExist()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_travel_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_system_list").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_system_description").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_description").assertDoesNotExist()

        onNodeWithText(text = "Mars").performClick()
        onNodeWithText(text = "app_yes").performClick()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome_start").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_skip").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_attributes_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_next").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_next").assertIsDisplayed()

        onNodeWithText(text = "tutorial_screen__mechanics_welcome").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_welcome_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_title").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_goal_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_ship_stats").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_travel_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_travel_description").assertDoesNotExist()
        onNodeWithTag(testTag = "tutorial_system_list").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_system_description").assertDoesNotExist()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_title").assertIsDisplayed()
        onNodeWithText(text = "tutorial_screen__mechanics_game_over_description").assertIsDisplayed()
    }
}
