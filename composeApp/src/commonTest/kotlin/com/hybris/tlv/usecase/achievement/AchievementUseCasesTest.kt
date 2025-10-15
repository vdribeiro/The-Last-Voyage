package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class AchievementUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get achievements`() = runBlocking {
        assertTrue(actual = testDependency.useCases.achievement.getAchievements().isEmpty())
        testDependency.useCases.achievement.syncAchievements()
        assertTrue(actual = testDependency.useCases.achievement.getAchievements().isNotEmpty())
    }
}
