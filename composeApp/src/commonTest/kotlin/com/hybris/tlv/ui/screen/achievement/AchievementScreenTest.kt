package com.hybris.tlv.ui.screen.achievement

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalTestApi::class)
internal class AchievementScreenTest {

    @BeforeTest
    fun setup() = runComposeUiTest {
        reset()
    }

    @Test
    fun achievementWithoutData() = runComposeUiTest {
        val store = getStoreFactory().createAchievementStore()
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
        runBlocking { getUseCases().achievement.syncAchievements() }
        val store = getStoreFactory().createAchievementStore()
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
