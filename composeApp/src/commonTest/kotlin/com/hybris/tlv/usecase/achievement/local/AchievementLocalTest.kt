package com.hybris.tlv.usecase.achievement.local

import com.hybris.tlv.Tester
import com.hybris.tlv.mock.achievements
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class AchievementLocalTest: Tester() {

    @Test
    fun `write and get achievements`() = runBlocking {
        assertTrue(actual = achievementDao.isAchievementEmpty())
        achievementDao.rewriteAchievements(achievements = achievements)
        assertEquals(expected = achievements, actual = achievementDao.getAchievements())
    }
}
