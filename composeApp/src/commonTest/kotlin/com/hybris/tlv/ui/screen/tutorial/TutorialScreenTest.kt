package com.hybris.tlv.screen.tutorial

import kotlin.test.BeforeTest
import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.getTutorialStore
import com.hybris.tlv.reset
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalTestApi::class)
internal class TutorialScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun tutorial() = runComposeUiTest {
        val store = getTutorialStore()
        setContent {
            AppTheme {
                TutorialScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = TUTORIAL_SCREEN).assertExists()
//        onNodeWithTag(testTag = TUTORIAL_SCREEN_STATUS_BAR).assertExists()
//        onNodeWithTag(testTag = TUTORIAL_SCREEN_NAVIGATION_BAR).assertExists()
    }
}
