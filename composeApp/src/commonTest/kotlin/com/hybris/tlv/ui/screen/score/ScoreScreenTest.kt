package com.hybris.tlv.ui.screen.score

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.count

@OptIn(ExperimentalTestApi::class)
internal class ScoreScreenTest: TestCase() {

    @Test
    fun scoreWithoutData() = runUITest {
        val store = storeFactory.get().getScoreStore()
        setUI { ScoreScreen(store = store) }

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
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val latestGameSession = dependency.get().useCases.gameSession.getLatestGameSession()!!
        dependency.get().useCases.gameSession.updateGameSession(gameSession = latestGameSession.copy(score = 9000.0))
        val store = storeFactory.get().getScoreStore()
        setUI { ScoreScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "score_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "score_list").assertIsDisplayed()
        onNodeWithTag(testTag = "score_list").count(count = 1)
    }
}
