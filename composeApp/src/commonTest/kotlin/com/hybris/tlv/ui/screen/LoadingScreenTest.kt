package com.hybris.tlv.ui.screen

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.hybris.tlv.test.TestCase

@OptIn(ExperimentalTestApi::class)
internal class LoadingScreenTest: TestCase() {

    @Test
    fun loading() = runUITest {
        setUI { LoadingScreen() }

        onNodeWithTag(testTag = "loading_foreground").assertIsDisplayed()
    }
}