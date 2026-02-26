package com.hybris.tlv.ui.screen.achievement

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.count

@OptIn(ExperimentalTestApi::class)
internal class AchievementScreenTest: TestCase() {

    @Test
    fun achievementWithoutData() = runUITest {
        val store = storeFactory.get().getAchievementStore()
        setUI { AchievementScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "achievements_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "achievement_list").assertIsDisplayed()
        onNodeWithTag(testTag = "achievement_list").count(count = 0)
    }

    @Test
    fun achievementWithData() = runUITest {
        dependency.get().useCases.achievement.prepopulateAchievements()
        val store = storeFactory.get().getAchievementStore()
        setUI { AchievementScreen(store = store) }

        onNodeWithTag(testTag = "topbar_back").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_help").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_music").assertIsDisplayed()
        onNodeWithTag(testTag = "topbar_feedback").assertIsDisplayed()

        onNodeWithText(text = "achievements_screen__title").assertIsDisplayed()
        onNodeWithTag(testTag = "achievement_list").assertIsDisplayed()
        onNodeWithTag(testTag = "achievement_list").count(count = FakeData.achievements.get().size)
    }
}
