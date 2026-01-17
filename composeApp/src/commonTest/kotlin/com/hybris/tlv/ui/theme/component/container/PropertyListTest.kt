package com.hybris.tlv.ui.theme.component.container

import kotlin.test.Test
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.hybris.tlv.TestCase
import com.hybris.tlv.ui.theme.component.text.Text

@OptIn(ExperimentalTestApi::class)
internal class PropertyListTest: TestCase() {

    @Test
    fun titleAndItems() = runUITest {
        val items = listOf("Alpha", "Beta", "Gamma")
        val title = "Star Properties"

        setScreen {
            PropertyList(
                title = title,
                properties = items,
                id = { it },
                description = { "description $it" }
            )
        }

        onNodeWithText(text = title).assertIsDisplayed()
        items.forEach { id ->
            onNodeWithText(text = id).assertExists()
            onNodeWithText(text = "description $id").assertExists()
        }
    }

    @Test
    fun headerAndFooter() = runUITest {
        setScreen {
            PropertyList(
                properties = listOf("Item 1"),
                id = { it },
                header = { Text(text = "Header Content") },
                footer = { Text(text = "Footer Content") }
            )
        }

        onNodeWithText(text = "Header Content").assertIsDisplayed()
        onNodeWithText(text = "Footer Content").assertIsDisplayed()
    }

    @Test
    fun scroll() = runUITest {
        val items = (1..50).map { "Property $it" }

        setScreen {
            PropertyList(
                modifier = Modifier.testTag(tag = "property_list_column"),
                properties = items,
                id = { it }
            )
        }

        onNodeWithText(text = "Property 1").assertIsDisplayed()
        onNodeWithText(text = "Property 50").assertDoesNotExist()
        onNodeWithTag(testTag = "property_list_column").performTouchInput { swipeUp() }
        onNodeWithText(text = "Property 50").assertExists()
    }
}
