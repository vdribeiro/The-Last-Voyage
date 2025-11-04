package com.hybris.tlv.ui.screen.tutorial

import kotlin.test.BeforeTest
import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalTestApi::class)
internal class TutorialScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testDependency.sqlDriver.clearDatabase()
    }

    @Test
    fun tutorial() = runComposeUiTest {
        val store = testDependency.storeFactory.createTutorialStore()
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
