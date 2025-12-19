package com.hybris.tlv.screen.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements
import com.hybris.tlv.navigation.Screen

internal class AchievementStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.achievement.prepopulateAchievements()
        val store = storeFactory.getAchievementStore()
        assertEquals(expected = false, actual = store.state().loading)
        assertEquals(expected = achievements, actual = store.state().achievements)
    }

    @Test
    fun initWithoutAchievements() = runUnitTest {
        val store = storeFactory.getAchievementStore()
        assertEquals(expected = false, actual = store.state().loading)
        assertEquals(expected = emptyList(), actual = store.state().achievements)
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        navigate(screen = Screen.Achievement)
        assertNavigationBackstack(list = listOf(element = Screen.Achievement))
        val store = storeFactory.getAchievementStore()
        store.back()
        assertNavigationBackstack(list = emptyList())
    }
}
