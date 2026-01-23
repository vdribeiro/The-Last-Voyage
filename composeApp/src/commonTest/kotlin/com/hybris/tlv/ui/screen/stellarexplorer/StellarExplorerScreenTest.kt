package com.hybris.tlv.ui.screen.stellarexplorer

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

@OptIn(ExperimentalTestApi::class)
internal class StellarExplorerScreenTest: TestCase() {

    @Test
    fun stellarExplorerWithoutData() = runUITest {
        val store = storeFactory.getStellarExplorerStore()
        setScreen { StellarExplorerScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_control_panel").assertIsDisplayed()
        onNodeWithTag(testTag = "control_panel_view_change").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_host_list").assertIsDisplayed()
        onNodeWithTag(testTag = "stellar_explorer_planet_list").assertDoesNotExist()

        onNodeWithTag(testTag = "stellar_explorer_host_list").count(count = 0)
    }

    @Test
    fun stellarExplorerWithData() = runUITest {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val store = storeFactory.getStellarExplorerStore()
        setScreen { StellarExplorerScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_control_panel").assertIsDisplayed()
        onNodeWithTag(testTag = "control_panel_view_change").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_host_list").assertIsDisplayed()
        onNodeWithTag(testTag = "stellar_explorer_planet_list").assertDoesNotExist()

        onNodeWithTag(testTag = "stellar_explorer_host_list").count(count = FakeData.getStellarHosts().size)

        onNodeWithTag(testTag = "control_panel_view_change").performClick()

        onNodeWithTag(testTag = "stellar_explorer_host_list").assertDoesNotExist()
        onNodeWithTag(testTag = "stellar_explorer_planet_list").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_planet_list").count(count = FakeData.getPlanets().size)
    }
}
