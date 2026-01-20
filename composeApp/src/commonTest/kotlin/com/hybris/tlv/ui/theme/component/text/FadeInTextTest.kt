package com.hybris.tlv.ui.theme.component.text

import kotlin.test.Test
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import com.hybris.tlv.test.TestCase

@OptIn(ExperimentalTestApi::class)
internal class FadeInTextTest: TestCase() {

    @Test
    fun animation() = runUITest {
        val tag = "fade_in_text"
        val text = "Hello World"
        val duration = 1000

        setScreen {
            FadeInText(
                modifier = Modifier.testTag(tag = tag),
                text = text,
                duration = duration
            )
        }

        mainClock.advanceTimeBy(milliseconds = (duration * 2).toLong())

        onNodeWithTag(testTag = tag)
            .assertExists()
            .assertTextEquals(text)
            .assertIsDisplayed()
    }
}