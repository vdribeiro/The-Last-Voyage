package com.hybris.tlv.ui.screen.score

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class ScoreScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.sqlDriver.clearDatabase()
    }

    @Test
    fun scoreWithoutData() = runComposeUiTest {
        val store = storeFactory.createScoreStore()
        setContent {
            AppTheme {
                ScoreScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = SCORE_SCREEN).assertExists()
        onNodeWithTag(testTag = SCORE_SCREEN_TITLE).assertExists()
        onNodeWithTag(testTag = SCORE_SCREEN_SCORES).assertExists()
        onNodeWithTag(testTag = SCORE_SCREEN_SCORE).assertDoesNotExist()
    }

    @Test
    fun scoreWithData() = runComposeUiTest {
        runBlocking {
            testCore.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
            val gameSession = testCore.useCases.gameSession.getLatestGameSession()!!
            testCore.useCases.gameSession.updateGameSession(gameSession = gameSession.copy(score = 9000.0))
        }
        val store = storeFactory.createScoreStore()
        setContent {
            AppTheme {
                ScoreScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = SCORE_SCREEN).assertExists()
        onNodeWithTag(testTag = SCORE_SCREEN_TITLE).assertExists()
        onNodeWithTag(testTag = SCORE_SCREEN_SCORES).assertExists()
        onNodeWithTag(testTag = SCORE_SCREEN_SCORE).assertExists()
    }
}
