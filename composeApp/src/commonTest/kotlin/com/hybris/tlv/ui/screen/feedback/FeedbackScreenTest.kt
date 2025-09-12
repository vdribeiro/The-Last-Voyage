package com.hybris.tlv.ui.screen.feedback

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import com.hybris.tlv.storeFactory
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class FeedbackScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun feedbackNull() = runComposeUiTest {
        val store = storeFactory.createFeedbackStore()
        setContent {
            FeedbackScreen(store = store)
        }

        onNodeWithTag(testTag = FEEDBACK_SCREEN).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_COLUMN).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_ICON).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_TITLE).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_DESCRIPTION).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_INPUT).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_BUTTON).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_THANKS).assertDoesNotExist()

        onNodeWithTag(testTag = FEEDBACK_SCREEN_INPUT).performTextInput(text = "MESSAGE")
        onNodeWithTag(testTag = FEEDBACK_SCREEN_BUTTON).performClick()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_THANKS).assertExists()
    }

    @Test
    fun feedback() = runComposeUiTest {
        val store = storeFactory.createFeedbackStore(
            state = FeedbackState(
                tag = "TAG",
                message = "MESSAGE"
            )
        )
        setContent {
            FeedbackScreen(store = store)
        }

        onNodeWithTag(testTag = FEEDBACK_SCREEN).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_COLUMN).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_ICON).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_TITLE).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_DESCRIPTION).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_INPUT).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_BUTTON).assertExists()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_THANKS).assertDoesNotExist()

        onNodeWithTag(testTag = FEEDBACK_SCREEN_INPUT).performTextInput(text = "MESSAGE")
        onNodeWithTag(testTag = FEEDBACK_SCREEN_BUTTON).performClick()
        onNodeWithTag(testTag = FEEDBACK_SCREEN_THANKS).assertExists()
    }
}
