package com.hybris.tlv.usecase.achievement

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class AchievementUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and get achievements`() = runBlocking {
        assertTrue(actual = mock.useCases.achievement.getAchievements().isEmpty())
        mock.internalAchievement.prepopulateAchievements()
        assertTrue(actual = mock.useCases.achievement.getAchievements().isNotEmpty())
    }

    @Test
    fun `rewrite and sync achievements`() = runBlocking {
        assertTrue(actual = mock.useCases.achievement.getAchievements().isEmpty())
        mock.internalAchievement.rewriteAchievements().last()
        assertTrue(actual = mock.useCases.achievement.getAchievements().isNotEmpty())
        mock.clearDatabase()
        assertTrue(actual = mock.useCases.achievement.getAchievements().isEmpty())
        mock.internalAchievement.syncAchievements().last()
        assertTrue(actual = mock.useCases.achievement.getAchievements().isNotEmpty())
    }
}
