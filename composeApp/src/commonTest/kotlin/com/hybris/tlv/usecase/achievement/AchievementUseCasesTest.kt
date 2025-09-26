package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mockCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class AchievementUseCasesTest {

    @BeforeTest
    fun setup() {
        mockCore.sqlDriver.clearDatabase()
        mockCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get achievements`() = runBlocking {
        assertTrue(actual = mockCore.useCases.achievement.getAchievements().isEmpty())
        mockCore.useCases.achievement.syncAchievements()
        assertTrue(actual = mockCore.useCases.achievement.getAchievements().isNotEmpty())
    }

    @Test
    fun `prepopulate and get achievements`() = runBlocking {
        assertTrue(actual = mockCore.useCases.achievement.getAchievements().isEmpty())
        mockCore.useCases.achievement.prepopulateAchievements()
        assertTrue(actual = mockCore.useCases.achievement.getAchievements().isNotEmpty())
    }
}
