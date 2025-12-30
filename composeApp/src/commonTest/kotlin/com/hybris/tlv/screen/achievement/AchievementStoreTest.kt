package com.hybris.tlv.screen.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements
import com.hybris.tlv.navigation.Screen

internal class AchievementStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.achievement.prepopulateAchievements()
        val store = storeFactory.getAchievementStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = achievements, actual = store.state.achievements)
    }

    @Test
    fun initWithoutAchievements() = runUnitTest {
        val store = storeFactory.getAchievementStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = emptyList(), actual = store.state.achievements)
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Achievement)
        assertNavigation(list = listOf(Screen.Achievement))
        storeFactory.getAchievementStore().back()
        assertNavigation(list = emptyList())
    }
}
