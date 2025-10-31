package com.hybris.tlv.usecase.achievement

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency

internal class AchievementUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.resetLocalConfigs()
    }

    @Test
    fun `sync and get achievements`() = runBlocking {
        assertTrue(actual = testDependency.useCases.achievement.getAchievements().isEmpty())
        testDependency.useCases.achievement.syncAchievements()
        assertTrue(actual = testDependency.useCases.achievement.getAchievements().isNotEmpty())
    }
}
