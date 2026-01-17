package com.hybris.tlv.theme.component.list

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hybris.tlv.TestCase

@OptIn(ExperimentalTestApi::class)
internal class StellarHostListTest: TestCase() {

    @Test
    fun planetHeaderVisibility() = runUITest {
        val planetName = "Kepler-442b"
        val showPlanet = mutableStateOf(value = true)
        setScreen {
            StellarHostList(
                showPlanet = showPlanet.value,
                planetName = planetName,
                stellarHosts = listOf("Kepler-442"),
                stellarHostName = { it }
            )
        }

        onNodeWithText(text = planetName).assertIsDisplayed()
        showPlanet.value = false
        onNodeWithText(text = planetName).assertDoesNotExist()
    }

    @Test
    fun stellarHostClick() = runUITest {
        val items = listOf("Sol", "Alpha Centauri A", "Sirius")
        var clickedHost: String? = null

        setScreen {
            StellarHostList(
                stellarHosts = items,
                stellarHostId = { it },
                stellarHostName = { it },
                onStellarHostClick = { clickedHost = it }
            )
        }

        items.forEach { name -> onNodeWithText(text = name).assertIsDisplayed() }
        onNodeWithText(text = "Sirius").performClick()
        assertEquals(expected = "Sirius", actual = clickedHost)
    }
}