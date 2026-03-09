package com.hybris.tlv.ui.theme.component.topbar

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.hybris.tlv.test.TestCase

@OptIn(ExperimentalTestApi::class)
internal class ControlPanelTest: TestCase() {

    @Test
    fun searchInputDebounce() = runUITest {
        var lastSearchValue = ""
        setUI {
            ControlPanel(onSearch = { lastSearchValue = it })
        }

        onNodeWithContentDescription(label = "Search").performTextInput(text = "Sun")
        assertEquals(expected = "", actual = lastSearchValue)
        waitUntil(timeoutMillis = 1000) { lastSearchValue == "Sun" }
        assertEquals(expected = "Sun", actual = lastSearchValue)
    }

    @Test
    fun sort() = runUITest {
        var isAscending = true
        setUI {
            ControlPanel(
                properties = persistentListOf("name" to "Name"),
                ascending = isAscending,
                onSortDirectionChange = { isAscending = !isAscending }
            )
        }

        onNodeWithContentDescription(label = "Sort Directions").performClick()
        assertFalse(actual = isAscending)
    }

    @Test
    fun visibility() = runUITest {
        var toggledProperty = ""
        val properties = persistentListOf("name" to "Name", "type" to "Type")

        setUI {
            ControlPanel(
                properties = properties,
                onVisibilityChange = { toggledProperty = it }
            )
        }

        onNodeWithContentDescription(label = "Visibility Options").performClick()
        onNodeWithText(text = "name").performClick()
        assertEquals(expected = "name", actual = toggledProperty)
    }

    @Test
    fun viewChangeResetsSearchQuery() = runUITest {
        setUI {
            ControlPanel(
                viewName = "Planets",
                viewIcon = Icons.Default.Public
            )
        }

        onNodeWithContentDescription(label = "Search").performTextInput(text = "Star")
        onNodeWithTag(testTag = "control_panel_view_change").performClick()
        onNodeWithContentDescription(label = "Search").assertTextContains(value = "")
    }
}