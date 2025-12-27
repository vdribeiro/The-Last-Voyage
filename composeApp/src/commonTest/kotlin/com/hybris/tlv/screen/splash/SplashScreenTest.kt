package com.hybris.tlv.screen.splash

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.TestCase
import com.hybris.tlv.screen.stellarexplorer.StellarExplorerScreen

@OptIn(ExperimentalTestApi::class)
internal class SplashScreenTest: TestCase() {

    @Test
    fun splash() = runUITest {
        val store = storeFactory.getSplashStore(reset = true)
        setScreen { SplashScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_music").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_feedback").assertDoesNotExist()

        onNodeWithText(text = "splash_screen__loading").assertIsDisplayed()
    }
}
