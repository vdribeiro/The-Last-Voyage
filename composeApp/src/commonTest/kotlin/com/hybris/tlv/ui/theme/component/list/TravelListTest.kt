package com.hybris.tlv.ui.theme.component.list

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.theme.component.text.Text

@OptIn(ExperimentalTestApi::class)
internal class TravelListTest: TestCase() {

    @Test
    fun click() = runUITest {
        val hosts = listOf("Sirius", "Vega", "Rigel")
        var lastClickedHost: String? = null

        setUI {
            TravelList(
                stellarHosts = hosts,
                id = { it },
                name = { it },
                distance = { 8.6 },
                onClick = { lastClickedHost = it }
            )
        }

        onNodeWithText(text = "Vega").performClick()
        assertEquals(expected = "Vega", actual = lastClickedHost)
    }

    @Test
    fun footer() = runUITest {
        val footerTag = "list_footer"

        setUI {
            TravelList(
                stellarHosts = listOf("Single Host"),
                name = { it },
                footer = { Text(modifier = Modifier.testTag(tag = footerTag), text = "End of List") }
            )
        }

        onNodeWithTag(testTag = footerTag).assertIsDisplayed()
        onNodeWithText(text = "End of List").assertExists()
    }
}