package com.hybris.tlv.ui.theme.component.container

import kotlin.test.Test
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.test.TestCase

internal class ConsoleTest: TestCase() {

    @Test
    fun titleAndLogs() = runUITest {
        val logContent = "This is a very nice log!"

        setUI {
            Console(logs = logContent)
        }

        onNodeWithText(text = "error_screen__console").assertIsDisplayed()
        onNodeWithText(text = logContent).assertIsDisplayed()
    }

    @Test
    fun nullLogs() = runUITest {
        setUI {
            Console(logs = null)
        }

        onNodeWithText(text = "error_screen__console").assertIsDisplayed()
        onNodeWithTag(testTag = "console_logs").assertIsDisplayed()
    }

    @Test
    fun scrollsToBottomOnNewLogs() = runUITest {
        val longLogs = (1..1000).joinToString(separator = "\n") { "Log Line $it" }
        val finalLine = "The Very Last Line"
        val logs = "$longLogs\n$finalLine"

        setUI {
            Console(logs = logs)
        }

        onNodeWithText(text = finalLine, substring = true).assertIsDisplayed()
    }
}
