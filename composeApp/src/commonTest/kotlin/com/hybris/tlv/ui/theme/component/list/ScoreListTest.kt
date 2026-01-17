package com.hybris.tlv.ui.theme.component.list

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class ScoreListTest: TestCase() {

    @Test
    fun expand() = runUITest {
        val items = listOf("Score A")

        setScreen {
            ScoreList(
                scores = items,
                id = { it },
                scorePoints = { 100.0 },
                fuel = { 80 }
            )
        }

        onNodeWithText(text = "ship_fuel: 80").assertDoesNotExist()
        onNodeWithText(text = "100.0").performClick()
        waitForIdle()
        onNodeWithText(text = "ship_fuel: 80").assertIsDisplayed()
        onNodeWithText(text = "100.0").performClick()
        waitForIdle()
        onNodeWithText(text = "ship_fuel: 80").assertDoesNotExist()
    }

    @Test
    fun expanded() = runUITest {
        val items = listOf("Score A", "Score B")

        setScreen {
            ScoreList(
                scores = items,
                expandedItems = listOf("Score A"),
                id = { it },
                settledPlanet = { "Earth" }
            )
        }
        onNodeWithText(text = "score_screen__title").assertIsDisplayed()
        onNodeWithText(text = "settled_planet: Earth").assertIsDisplayed()
    }
}