package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class AchievementUseCasesTest {

    @BeforeTest
    fun setup() {
        testCore.sqlDriver.clearDatabase()
        testCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get achievements`() = runBlocking {
        assertTrue(actual = testCore.useCases.achievement.getAchievements().isEmpty())
        testCore.useCases.achievement.syncAchievements()
        assertTrue(actual = testCore.useCases.achievement.getAchievements().isNotEmpty())
    }

    @Test
    fun `prepopulate and get achievements`() = runBlocking {
        assertTrue(actual = testCore.useCases.achievement.getAchievements().isEmpty())
        testCore.useCases.achievement.prepopulateAchievements()
        assertTrue(actual = testCore.useCases.achievement.getAchievements().isNotEmpty())
    }
}
