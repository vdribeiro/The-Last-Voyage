package com.hybris.tlv.screen.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements
import com.hybris.tlv.getAchievementStore
import com.hybris.tlv.state
import com.hybris.tlv.useCases

internal class AchievementStoreTest: TestCase() {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init`() = runUnitTest {
        useCases.achievement.syncAchievements()
        val achievementStore = getAchievementStore()
        assertEquals(expected = false, actual = achievementStore.state().loading)
        assertEquals(expected = achievements, actual = achievementStore.state().achievements)
    }
}
