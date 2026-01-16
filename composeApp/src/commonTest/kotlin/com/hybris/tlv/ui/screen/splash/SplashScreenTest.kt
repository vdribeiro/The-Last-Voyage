package com.hybris.tlv.ui.screen.splash

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class SplashScreenTest: TestCase() {

    @Test
    fun splash() = TestCase.runUITest {
        val store = TestCase.storeFactory.getSplashStore(reset = true)
        setScreen { SplashScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_feedback").assertDoesNotExist()
    }
}
