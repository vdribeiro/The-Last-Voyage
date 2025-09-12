package com.hybris.tlv.ui.screen.achievement

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.storeFactory
import kotlin.test.Test

// TODO: add missing tests
@OptIn(ExperimentalTestApi::class)
internal class AchievementScreenTest {

    @Test
    fun test() = runComposeUiTest {
        setContent {
            AchievementScreen(store = storeFactory.createAchievementStore())
        }

        onNodeWithTag(testTag = "list").assertExists()
    }
}
