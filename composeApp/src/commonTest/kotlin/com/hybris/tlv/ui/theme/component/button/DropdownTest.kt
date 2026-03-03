package com.hybris.tlv.ui.theme.component.button

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.TestCase

@OptIn(ExperimentalTestApi::class)
internal class DropdownTest: TestCase() {

    @Test
    fun expanded() = runUITest {
        val items = persistentListOf("Mercury", "Venus", "Mars")
        val expanded = mutableStateOf(value = true)

        setUI {
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
        val items = persistentListOf("Earth", "Jupiter")
        var capturedItem: String? = null

        setUI {
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
        val items = persistentListOf("Saturn")
        var clickCount = 0

        setUI {
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