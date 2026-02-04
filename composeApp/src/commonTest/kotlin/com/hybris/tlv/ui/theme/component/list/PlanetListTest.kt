package com.hybris.tlv.ui.theme.component.list

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.TestCase

@OptIn(ExperimentalTestApi::class)
internal class PlanetListTest: TestCase() {

    @Test
    fun stellarHostVisibility() = runUITest {
        val hostName = "Proxima Centauri"
        val showStellarHost = mutableStateOf(value = true)
        setUI {
            PlanetList(
                showStellarHost = showStellarHost.value,
                stellarHostName = hostName,
                planets = listOf("Planet b"),
                planetName = { it }
            )
        }

        onNodeWithText(text = hostName).assertIsDisplayed()
        showStellarHost.value = false
        onNodeWithText(text = hostName).assertDoesNotExist()
    }

    @Test
    fun planetClick() = runUITest {
        val items = listOf("Kepler-186f", "TRAPPIST-1e")
        var clickedPlanet: String? = null

        setUI {
            PlanetList(
                planets = items,
                planetId = { it },
                planetName = { it },
                onPlanetClick = { clickedPlanet = it }
            )
        }

        items.forEach { name -> onNodeWithText(text = name).assertIsDisplayed() }
        onNodeWithText(text = "Kepler-186f").performClick()
        assertEquals(expected = "Kepler-186f", actual = clickedPlanet)
    }
}