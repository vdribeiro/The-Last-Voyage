package com.hybris.tlv.theme.component.list

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class SystemListTest: TestCase() {

    @Test
    fun dialog() = runUITest {
        val planetName = "Mars"
        var settlementConfirmed = false

        setScreen {
            SystemList(
                planets = listOf(planetName),
                planetName = { it },
                onClick = { settlementConfirmed = true }
            )
        }

        onNodeWithText(text = planetName).performClick()
        waitForIdle()
        onNodeWithText(text = "app_no").performClick()
        onNodeWithText(text = planetName).assertIsDisplayed()

        onNodeWithText(text = "game_screen__settle $planetName?").assertIsDisplayed()
        onNodeWithText(text = "app_yes").performClick()
        assertTrue(actual = settlementConfirmed)
        onNodeWithText(text = "app_yes").assertDoesNotExist()
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
}