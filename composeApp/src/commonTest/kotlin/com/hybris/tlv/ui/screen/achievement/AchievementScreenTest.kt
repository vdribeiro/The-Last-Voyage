package com.hybris.tlv.ui.screen.achievement

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.achievements
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.theme.AppTheme
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
internal class AchievementScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        testCore.clearDatabase()
    }

    @Test
    fun achievementWithoutData() = runComposeUiTest {
        val store = storeFactory.createAchievementStore()
        setContent {
            AppTheme {
                AchievementScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN).assertExists()
        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN_LIST).assertExists()
        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN_LIST_ITEM).assertDoesNotExist()
    }

    @Test
    fun achievementWithData() = runComposeUiTest {
        runBlocking { testCore.useCases.achievement.prepopulateAchievements() }
        val store = storeFactory.createAchievementStore()
        setContent {
            AppTheme {
                AchievementScreen(store = store)
            }
        }
        waitForIdle()

        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN).assertExists()
        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN_LIST).assertExists()
        onAllNodesWithTag(testTag = ACHIEVEMENT_SCREEN_LIST_ITEM)
            .assertCountEquals(expectedSize = achievements.size)
    }
}
