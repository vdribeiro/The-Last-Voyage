package com.hybris.tlv.ui.screen.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements
import com.hybris.tlv.ui.navigation.Screen

internal class AchievementStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.achievement.prepopulateAchievements()
        val store = TestCase.storeFactory.getAchievementStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = achievements, actual = store.state.achievements)
    }

    @Test
    fun initWithoutAchievements() = TestCase.runUnitTest {
        val store = TestCase.storeFactory.getAchievementStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.achievements.isEmpty())
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Achievement)
        TestCase.assertNavigation(list = listOf(Screen.Achievement))
        TestCase.storeFactory.getAchievementStore().back()
        TestCase.assertNavigation(list = emptyList())
    }
}
