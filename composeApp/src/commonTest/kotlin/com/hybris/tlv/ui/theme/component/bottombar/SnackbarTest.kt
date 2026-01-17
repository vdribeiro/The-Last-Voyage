package com.hybris.tlv.ui.theme.component.bottombar

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class SnackbarTest: TestCase() {

    @Test
    fun displaysMessageThenAutoDismisses() = runUITest {
        var dismissCalled = false
        val message = "Hello World"
        val duration = 2000L

        setScreen {
            Snackbar(
                message = message,
                durationMillis = duration,
                onDismiss = { dismissCalled = true }
            )
        }

        onNodeWithText(text = message).assertIsDisplayed()
        assertFalse(actual = dismissCalled)
        waitUntil(timeoutMillis = duration * 2) { onAllNodesWithText(text = message).fetchSemanticsNodes().isEmpty() && dismissCalled }
        onNodeWithText(text = message).assertDoesNotExist()
        assertTrue(actual = dismissCalled)
    }

    @Test
    fun button() = runUITest {
        val message = "Hello World"
        val actionText = "Goodbye"

        setScreen {
            Snackbar(
                message = message,
                buttonText = actionText
            )
        }

        onNodeWithText(text = actionText)
            .assertIsDisplayed()
            .performClick()
    }
}