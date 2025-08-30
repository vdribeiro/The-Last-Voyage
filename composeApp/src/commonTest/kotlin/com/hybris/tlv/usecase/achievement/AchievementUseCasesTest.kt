package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class AchievementUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `prepopulate and get achievements`() = runBlocking {
        assertTrue(actual = mock.useCases.achievement.getAchievements().isEmpty())
        mock.internalAchievement.prepopulateAchievements()
        assertTrue(actual = mock.useCases.achievement.getAchievements().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync achievements`() = runBlocking {
        assertTrue(actual = mock.useCases.achievement.getAchievements().isEmpty())
        assertTrue(actual = mock.internalAchievement.syncAchievements() is SyncResult.Success)
        assertTrue(actual = mock.useCases.achievement.getAchievements().isNotEmpty())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.internalAchievement.syncAchievements() is SyncResult.Error)
    }
}
