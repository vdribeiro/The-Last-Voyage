package com.hybris.tlv.ui.screen.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class AchievementStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.achievement.prepopulateAchievements()
        val store = getStoreFactory().getAchievementStore()
        assertFalse(actual = store.state.loading)
        assertEquals(expected = FakeData.achievements.get(), actual = store.state.achievements)
    }

    @Test
    fun initWithoutAchievements() = runUnitTest {
        val store = getStoreFactory().getAchievementStore()
        assertFalse(actual = store.state.loading)
        assertTrue(actual = store.state.achievements.isEmpty())
    }
}
