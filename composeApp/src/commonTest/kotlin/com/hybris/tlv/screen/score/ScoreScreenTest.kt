package com.hybris.tlv.screen.score

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype

@OptIn(ExperimentalTestApi::class)
internal class ScoreScreenTest: TestCase() {

    @Test
    fun scoreWithoutData() = runUITest {
        val store = storeFactory.getScoreStore()
        setScreen { ScoreScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "score_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "score_list").assertIsDisplayed()
        onNodeWithTag(testTag = "score_list").count(count = 0)
    }

    @Test
    fun scoreWithData() = runUITest {
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val latestGameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val store = storeFactory.getScoreStore()
        setScreen { ScoreScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "score_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "score_list").assertIsDisplayed()
        onNodeWithTag(testTag = "score_list").count(count = 1)
    }
}
