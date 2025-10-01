package com.hybris.tlv.ui.screen.splash

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class SplashScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.sqlDriver.clearDatabase()
    }

    @Test
    fun splash() = runComposeUiTest {
        val store = storeFactory.createSplashStore()
        setContent {
            AppTheme {
                SplashScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = SPLASH_SCREEN).assertExists()
        onNodeWithTag(testTag = SPLASH_SCREEN_LOGO_BACKGROUND).assertExists()
        onNodeWithTag(testTag = SPLASH_SCREEN_LOGO).assertExists()
        onNodeWithTag(testTag = SPLASH_SCREEN_PROGRESS).assertExists()
        onNodeWithTag(testTag = SPLASH_SCREEN_LOADING).assertExists()
    }
}
