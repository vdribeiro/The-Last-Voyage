package com.hybris.tlv.ui.screen.catastropheexplorer

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.count

@OptIn(ExperimentalTestApi::class)
internal class CatastropheExplorerScreenTest: TestCase() {

    @Test
    fun catastropheWithoutData() = runUITest {
        val store = storeFactory.get().getCatastropheExplorerStore()
        setUI { CatastropheExplorerScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "catastrophe_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "catastrophe_list").assertIsDisplayed()

        onNodeWithTag(testTag = "catastrophe_list").count(count = 0)
    }

    @Test
    fun catastropheWithData() = runUITest {
        dependency.get().useCases.catastrophe.syncCatastrophes()
        val store = storeFactory.get().getCatastropheExplorerStore()
        setUI { CatastropheExplorerScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertDoesNotExist()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "catastrophe_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "catastrophe_list").assertIsDisplayed()

        onNodeWithTag(testTag = "catastrophe_list").count(count = FakeData.catastrophes.get().size)
    }
}
