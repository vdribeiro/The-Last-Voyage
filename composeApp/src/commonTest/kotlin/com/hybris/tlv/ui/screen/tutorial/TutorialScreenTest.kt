package com.hybris.tlv.ui.screen.tutorial

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class TutorialScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.clearDatabase()
    }

    @Test
    fun tutorial() = runComposeUiTest {
        val store = storeFactory.createTutorialStore()
        setContent {
            AppTheme {
                TutorialScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = TUTORIAL_SCREEN).assertExists()
        onNodeWithTag(testTag = TUTORIAL_SCREEN_STATUS_BAR).assertExists()
        onNodeWithTag(testTag = TUTORIAL_SCREEN_NAVIGATION_BAR).assertExists()
        onNodeWithTag(testTag = TUTORIAL_SCREEN_NAVIGATION_BAR_ITEM_SHIP).assertExists()
        onNodeWithTag(testTag = TUTORIAL_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM).assertExists()
        onNodeWithTag(testTag = TUTORIAL_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL).assertExists()
        onNodeWithTag(testTag = TUTORIAL_SCREEN_CONTENT).assertExists()
    }
}
