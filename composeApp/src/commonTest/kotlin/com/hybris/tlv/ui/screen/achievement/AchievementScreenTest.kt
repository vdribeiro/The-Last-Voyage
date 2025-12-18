package com.hybris.tlv.screen.achievement

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.getAchievementStore
import com.hybris.tlv.reset
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.useCases

@OptIn(ExperimentalTestApi::class)
internal class AchievementScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun achievementWithoutData() = runComposeUiTest {
        val store = getAchievementStore()
        setContent {
            AppTheme {
                AchievementScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN).assertExists()
//        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN_LIST).assertExists()
//        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN_LIST_ITEM).assertDoesNotExist()
    }

    @Test
    fun achievementWithData() = runComposeUiTest {
        runBlocking { useCases.achievement.syncAchievements() }
        val store = getAchievementStore()
        setContent {
            AppTheme {
                AchievementScreen(store = store)
            }
        }
        waitForIdle()

//        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN).assertExists()
//        onNodeWithTag(testTag = ACHIEVEMENT_SCREEN_LIST).assertExists()
//        onAllNodesWithTag(testTag = ACHIEVEMENT_SCREEN_LIST_ITEM)
//            .assertCountEquals(expectedSize = achievements.size)
    }
}
