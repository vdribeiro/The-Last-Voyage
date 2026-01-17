package com.hybris.tlv.ui.screen.catastrophe

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class CatastropheScreenTest: TestCase() {

    @Test
    fun catastropheWithoutData() = runUITest {
        val store = storeFactory.getCatastropheStore()
        setScreen { CatastropheScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "new_game_screen__continue").assertDoesNotExist()
        onNodeWithTag(testTag = "new_game_content").assertIsDisplayed()
    }

    @Test
    fun catastropheWithData() = runUITest {
        useCases.catastrophe.syncCatastrophes()
        val store = storeFactory.getCatastropheStore()
        setScreen { CatastropheScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "new_game_screen__continue").assertIsDisplayed()
        onNodeWithTag(testTag = "new_game_content").assertIsDisplayed()

        onNodeWithText(text = "new_game_screen__continue").performClick()
    }
}
