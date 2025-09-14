package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class AchievementUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
        mock.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get achievements`() = runBlocking {
        assertTrue(actual = mock.useCases.achievement.getAchievements().isEmpty())
        mock.useCases.achievement.syncAchievements()
        assertTrue(actual = mock.useCases.achievement.getAchievements().isNotEmpty())
    }

    @Test
    fun `prepopulate and get achievements`() = runBlocking {
        assertTrue(actual = mock.useCases.achievement.getAchievements().isEmpty())
        mock.useCases.achievement.prepopulateAchievements()
        assertTrue(actual = mock.useCases.achievement.getAchievements().isNotEmpty())
    }
}
