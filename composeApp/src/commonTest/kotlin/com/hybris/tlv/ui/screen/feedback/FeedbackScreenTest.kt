package com.hybris.tlv.ui.screen.feedback

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.hybris.tlv.test.TestCase

@OptIn(ExperimentalTestApi::class)
internal class FeedbackScreenTest: TestCase() {

    @Test
    fun feedback() = runUITest {
        val store = storeFactory.getFeedbackStore(tag = null, message = null)
        setScreen { FeedbackScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertDoesNotExist()

        onNodeWithContentDescription(label = "Feedback Icon").assertIsDisplayed()
        onNodeWithText(text = "error_screen__title_alt").assertIsDisplayed()
        onNodeWithText(text = "error_screen__description_alt").assertIsDisplayed()
        onNodeWithTag(testTag = "feedback_input").assertIsDisplayed()
        onNodeWithText(text = "error_screen__button").assertIsDisplayed()
        onNodeWithText(text = "error_screen__button").assertIsNotEnabled()
        onNodeWithText(text = "error_screen__thanks").assertDoesNotExist()

        onNodeWithTag(testTag = "feedback_input").performTextInput(text = "Feedback message")
        onNodeWithText(text = "error_screen__button").assertIsEnabled()
        onNodeWithText(text = "error_screen__button").performClick()
        onNodeWithText(text = "error_screen__thanks").assertIsDisplayed()
    }

    @Test
    fun feedbackError() = runUITest {
        val store = storeFactory.getFeedbackStore(tag = "tag", message = "message")
        setScreen { FeedbackScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertDoesNotExist()

        onNodeWithContentDescription(label = "Feedback Icon").assertIsDisplayed()
        onNodeWithText(text = "error_screen__title").assertIsDisplayed()
        onNodeWithText(text = "error_screen__description").assertIsDisplayed()
        onNodeWithTag(testTag = "feedback_input").assertIsDisplayed()
        onNodeWithText(text = "error_screen__button").assertIsDisplayed()
        onNodeWithText(text = "error_screen__button").assertIsEnabled()
        onNodeWithText(text = "error_screen__thanks").assertDoesNotExist()

        onNodeWithTag(testTag = "feedback_input").performTextInput(text = "Feedback message")
        onNodeWithText(text = "error_screen__button").performClick()
        onNodeWithText(text = "error_screen__thanks").assertIsDisplayed()
    }
}
