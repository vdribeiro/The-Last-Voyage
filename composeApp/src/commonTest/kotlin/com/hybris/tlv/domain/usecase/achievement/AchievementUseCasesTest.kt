package com.hybris.tlv.domain.usecase.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.domain.usecase.space.SUN
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class AchievementUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncAchievements() = runUnitTest {
        assertTrue(actual = dependency.get().useCases.achievement.getAchievements().isEmpty())
        dependency.get().useCases.achievement.prepopulateAchievements()
        assertEquals(expected = FakeData.achievements.get().sortedBy { it.id }, actual = dependency.get().useCases.achievement.getAchievements().sortedBy { it.id })

        resetData()
        assertTrue(actual = dependency.get().useCases.achievement.getAchievements().isEmpty())
        dependency.get().useCases.achievement.syncAchievements()
        assertEquals(expected = FakeData.achievements.get().sortedBy { it.id }, actual = dependency.get().useCases.achievement.getAchievements().sortedBy { it.id })
    }

    @Test
    fun updateAchievements() = runUnitTest {
        dependency.get().useCases.achievement.prepopulateAchievements()
        val gameSession = dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val newGameSession = gameSession.copy(
            currentStellarHostId = SUN,
            settledPlanetId = "3earth",
            finalHabitability = 0.9
        )
        assertTrue(actual = dependency.get().useCases.achievement.updateAchievements(gameSession = newGameSession).isNotEmpty())
    }
}
