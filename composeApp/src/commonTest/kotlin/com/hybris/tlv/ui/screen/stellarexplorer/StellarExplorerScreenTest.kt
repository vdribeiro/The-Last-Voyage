package com.hybris.tlv.ui.screen.stellarexplorer

import kotlin.test.Test
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.count

internal class StellarExplorerScreenTest: TestCase() {

    @Test
    fun stellarExplorerWithoutData() = runUITest {
        val store = storeFactory.get().getStellarExplorerStore()
        setUI { StellarExplorerScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_control_panel").assertIsDisplayed()
        onNodeWithTag(testTag = "control_panel_view_change").assertIsDisplayed()
        onNodeWithTag(testTag = "stellar_explorer_list").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_list").count(count = 0)
    }

    @Test
    fun stellarExplorerWithData() = runUITest {
        dependency.get().useCases.space.syncStellarHosts()
        dependency.get().useCases.space.syncPlanets()
        val store = storeFactory.get().getStellarExplorerStore()
        setUI { StellarExplorerScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_control_panel").assertIsDisplayed()
        onNodeWithTag(testTag = "control_panel_view_change").assertIsDisplayed()
        onNodeWithTag(testTag = "stellar_explorer_list").assertIsDisplayed()

        onNodeWithTag(testTag = "stellar_explorer_list").count(count = FakeData.stellarHosts.get().size)
    }
}
