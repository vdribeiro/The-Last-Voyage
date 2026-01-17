package com.hybris.tlv.ui.theme.component.bottombar

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class NavigationBarTest: TestCase() {

    @Test
    fun itemsAreDisplayed() = runUITest {
        val items = listOf("Home", "Search", "Settings")
        val selectedItem = "Search"
        var capturedItem: String? = null

        setScreen {
            NavigationBar(
                items = items,
                selected = { it == selectedItem },
                text = { it },
                onClick = { capturedItem = it }
            )
        }

        items.forEach { label -> onNodeWithText(text = label).assertIsDisplayed() }
        onNodeWithText(text = "Home").assertIsNotSelected()
        onNodeWithText(text = "Search").assertIsSelected()
        onNodeWithText(text = "Settings").assertIsNotSelected()

        onNodeWithText(text = "Home").performClick()
        assertEquals(expected = "Home", actual = capturedItem)
    }

    @Test
    fun disabled() = runUITest {
        val items = listOf("Home")
        var clickCount = 0

        setScreen {
            NavigationBar(
                items = items,
                enabled = { false },
                text = { it },
                onClick = { clickCount++ }
            )
        }

        onNodeWithText(text = "Home").performClick()
        assertEquals(expected = 0, actual = clickCount)
    }
}
