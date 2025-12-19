package com.hybris.tlv.screen.score

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.TestCase

// TODO
@OptIn(ExperimentalTestApi::class)
internal class ScoreScreenTest: TestCase() {

    @Test
    fun scoreWithoutData() = runComposeUiTest {
//        val store = getScoreStore()
//        setContent {
//            AppTheme {
//                ScoreScreen(store = store)
//            }
//        }
//        waitForIdle()

//        onNodeWithTag(testTag = SCORE_SCREEN).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_TITLE).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_SCORES).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_SCORE).assertDoesNotExist()
    }

    @Test
    fun scoreWithData() = runComposeUiTest {
//        runBlocking {
//            useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//            val gameSession = useCases.gameSession.getLatestGameSession()!!
//            useCases.gameSession.updateGameSession(gameSession = gameSession.copy(score = 9000.0))
//        }
//        val store = getScoreStore()
//        setContent {
//            AppTheme {
//                ScoreScreen(store = store)
//            }
//        }
//        waitForIdle()

//        onNodeWithTag(testTag = SCORE_SCREEN).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_TITLE).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_SCORES).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_SCORE).assertExists()
    }
}
