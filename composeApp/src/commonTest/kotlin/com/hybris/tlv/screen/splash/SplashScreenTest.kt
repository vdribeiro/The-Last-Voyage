package com.hybris.tlv.screen.splash

import kotlin.test.BeforeTest
import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.getSplashStore
import com.hybris.tlv.reset
import com.hybris.tlv.theme.AppTheme

@OptIn(ExperimentalTestApi::class)
internal class SplashScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun splash() = runComposeUiTest {
        val store = getSplashStore()
        setContent {
            AppTheme {
                SplashScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = SPLASH_SCREEN).assertExists()
    }
}
