package com.hybris.tlv.ui.theme.component.button

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class DropdownTest: TestCase() {

    @Test
    fun expanded() = runUITest {
        val items = listOf("Mercury", "Venus", "Mars")
        val expanded = mutableStateOf(value = true)

        setScreen {
            Dropdown(
                expanded = expanded.value,
                items = items,
                text = { it }
            )
        }

        items.forEach { onNodeWithText(text = it).assertIsDisplayed() }
        expanded.value = false
        waitForIdle()
        items.forEach { onNodeWithText(text = it).assertDoesNotExist() }
    }

    @Test
    fun itemClick() = runUITest {
        val items = listOf("Earth", "Jupiter")
        var capturedItem: String? = null

        setScreen {
            Dropdown(
                expanded = true,
                items = items,
                text = { it },
                onClick = { capturedItem = it }
            )
        }

        onNodeWithText(text = "Jupiter").performClick()
        assertEquals(expected = "Jupiter", actual = capturedItem)
    }

    @Test
    fun disabled() = runUITest {
        val items = listOf("Saturn")
        var clickCount = 0

        setScreen {
            Dropdown(
                expanded = true,
                items = items,
                enabled = { false },
                text = { it },
                onClick = { clickCount++ }
            )
        }

        onNodeWithText(text = "Saturn").performClick()
        assertEquals(expected = 0, actual = clickCount)
    }
}