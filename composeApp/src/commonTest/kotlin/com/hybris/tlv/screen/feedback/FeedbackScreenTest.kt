package com.hybris.tlv.screen.feedback

import kotlin.test.BeforeTest
import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.getFeedbackStore
import com.hybris.tlv.reset
import com.hybris.tlv.theme.AppTheme

// TODO
@OptIn(ExperimentalTestApi::class)
internal class FeedbackScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun feedback() = runComposeUiTest {
        val store = getFeedbackStore()
        setContent {
            AppTheme {
                FeedbackScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = FEEDBACK_SCREEN).assertExists()
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_COLUMN).assertExists()
        //onNodeWithTag(testTag = FEEDBACK_SCREEN_ICON).assertExists()
        //onNodeWithTag(testTag = FEEDBACK_SCREEN_TITLE).assertExists().assertTextEquals("error_screen__title_alt")
        //onNodeWithTag(testTag = FEEDBACK_SCREEN_DESCRIPTION).assertExists().assertTextEquals("error_screen__description_alt")
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_INPUT).assertExists()
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_BUTTON).assertExists().assertTextEquals("error_screen__button")
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_THANKS).assertDoesNotExist()

//        onNodeWithTag(testTag = FEEDBACK_SCREEN_INPUT).performTextInput(text = "MESSAGE")
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_BUTTON).performClick()
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_THANKS).assertExists().assertTextEquals("error_screen__thanks")
    }

    @Test
    fun feedbackError() = runComposeUiTest {
        val store = getFeedbackStore(
            tag = "TAG",
            message = "MESSAGE"
        )
        setContent {
            AppTheme {
                FeedbackScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = FEEDBACK_SCREEN).assertExists()
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_COLUMN).assertExists()
        //onNodeWithTag(testTag = FEEDBACK_SCREEN_ICON).assertExists()
        //onNodeWithTag(testTag = FEEDBACK_SCREEN_TITLE).assertExists().assertTextEquals("error_screen__title")
        //onNodeWithTag(testTag = FEEDBACK_SCREEN_DESCRIPTION).assertExists().assertTextEquals("error_screen__description")
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_INPUT).assertExists()
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_BUTTON).assertExists().assertTextEquals("error_screen__button")
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_THANKS).assertDoesNotExist()

//        onNodeWithTag(testTag = FEEDBACK_SCREEN_INPUT).performTextInput(text = "MESSAGE")
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_BUTTON).performClick()
//        onNodeWithTag(testTag = FEEDBACK_SCREEN_THANKS).assertExists().assertTextEquals("error_screen__thanks")
    }
}
