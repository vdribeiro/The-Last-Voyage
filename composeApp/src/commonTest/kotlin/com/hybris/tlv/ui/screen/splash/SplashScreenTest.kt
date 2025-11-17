package com.hybris.tlv.ui.screen.splash

import kotlin.test.BeforeTest
import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.reset
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalTestApi::class)
internal class SplashScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun splash() = runComposeUiTest {
        val store = getStoreFactory().createSplashStore()
        setContent {
            AppTheme {
                SplashScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = SPLASH_SCREEN).assertExists()
    }
}
