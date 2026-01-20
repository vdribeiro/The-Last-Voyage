package com.hybris.tlv.ui.theme.component.list

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
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
internal class SystemListTest: TestCase() {

    @Test
    fun dialog() = runUITest {
        val planetName = "Mars"
        var settlementConfirmed = false

        setScreen {
            SystemList(
                stellarHostName = "Sol",
                planets = listOf(planetName),
                planetName = { it },
                onClick = { settlementConfirmed = true }
            )
        }

        onNodeWithText(text = planetName).performClick()
        waitForIdle()
        onNodeWithText(text = "app_no").performClick()
        waitForIdle()
        assertFalse(actual = settlementConfirmed)
        onNodeWithText(text = planetName).assertIsDisplayed()

        onNodeWithText(text = planetName).performClick()
        waitForIdle()

        onNodeWithText(text = "app_yes").performClick()
        waitForIdle()
        assertTrue(actual = settlementConfirmed)
    }

    @Test
    fun hostAndPlanets() = runUITest {
        val host = "Alpha Centauri"
        val planet = "Proxima b"

        setScreen {
            SystemList(
                stellarHostName = host,
                planets = listOf(planet),
                planetName = { it }
            )
        }

        onNodeWithText(text = host).assertIsDisplayed()
        onNodeWithText(text = planet).assertIsDisplayed()
    }

    @Test
    fun footer() = runUITest {
        val footerTag = "list_footer"

        setScreen {
            SystemList(
                stellarHostName = "Sol",
                planets = listOf("Mars"),
                planetName = { it },
                footer = { Text(modifier = Modifier.testTag(tag = footerTag), text = "End of List") }
            )
        }

        onNodeWithTag(testTag = footerTag).assertIsDisplayed()
        onNodeWithText(text = "End of List").assertExists()
    }
}