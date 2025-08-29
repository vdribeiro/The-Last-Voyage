package com.hybris.tlv.usecase.achievement.local

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.mock.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AchievementLocalTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `write and get achievements`() = runTest {
        assertTrue(actual = mock.achievementDao.isAchievementEmpty())
        mock.achievementDao.rewriteAchievements(achievements = achievements)
        assertEquals(expected = achievements, actual = mock.achievementDao.getAchievements())
    }
}
