package com.hybris.tlv.screen.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements
import com.hybris.tlv.navigation.Screen

internal class AchievementStoreTest: TestCase() {

    @Test
    fun initStore() = runUnitTest {
        useCases.achievement.prepopulateAchievements()
        val store = storeFactory.getAchievementStore()
        assertEquals(expected = false, actual = store.state().loading)
        assertEquals(expected = achievements, actual = store.state().achievements)
    }

    @Test
    fun `send action back`() = runUnitTest {
        assertTrue(actual = screens.isEmpty())
        navigate(screen = Screen.Achievement)
        assertEquals(expected = listOf(element = Screen.Achievement), actual = screens)
        val store = storeFactory.getAchievementStore()
        store.back()
        assertTrue(actual = screens.isEmpty())
    }
}
