package com.hybris.tlv.ui.screen.score

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getScoreStore
import com.hybris.tlv.reset
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.useCases

@OptIn(ExperimentalTestApi::class)
internal class ScoreScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun scoreWithoutData() = runComposeUiTest {
        val store = getScoreStore()
        setContent {
            AppTheme {
                ScoreScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = SCORE_SCREEN).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_TITLE).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_SCORES).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_SCORE).assertDoesNotExist()
    }

    @Test
    fun scoreWithData() = runComposeUiTest {
        runBlocking {
            useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
            val gameSession = useCases.gameSession.getLatestGameSession()!!
            useCases.gameSession.updateGameSession(gameSession = gameSession.copy(score = 9000.0))
        }
        val store = getScoreStore()
        setContent {
            AppTheme {
                ScoreScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = SCORE_SCREEN).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_TITLE).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_SCORES).assertExists()
//        onNodeWithTag(testTag = SCORE_SCREEN_SCORE).assertExists()
    }
}
