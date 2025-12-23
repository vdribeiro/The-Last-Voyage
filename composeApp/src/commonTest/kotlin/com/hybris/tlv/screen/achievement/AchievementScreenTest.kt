package com.hybris.tlv.screen.achievement

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements

@OptIn(ExperimentalTestApi::class)
internal class AchievementScreenTest: TestCase() {

    @Test
    fun achievementWithoutData() = runUITest {
        val store = storeFactory.getAchievementStore()
        setScreen { AchievementScreen(store = store) }

        onNodeWithText(text = "achievements_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "achievement_list").assertIsDisplayed()
        onNodeWithTag(testTag = "achievement_list")
            .onChildren()
            .assertCountEquals(expectedSize = 0)
    }

    @Test
    fun achievementWithData() = runUITest {
        useCases.achievement.prepopulateAchievements()
        val store = storeFactory.getAchievementStore()
        setScreen { AchievementScreen(store = store) }
        onNodeWithText(text = "achievements_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "achievement_list").assertIsDisplayed()
        onNodeWithTag(testTag = "achievement_list").count(count = achievements.size)
    }
}
