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
        assertTrue(actual = getUseCases().achievement.getAchievements().isEmpty())
        getUseCases().achievement.prepopulateAchievements()
        assertEquals(expected = FakeData.getAchievements().sortedBy { it.id }, actual = getUseCases().achievement.getAchievements().sortedBy { it.id })

        reset()
        assertTrue(actual = getUseCases().achievement.getAchievements().isEmpty())
        getUseCases().achievement.syncAchievements()
        assertEquals(expected = FakeData.getAchievements().sortedBy { it.id }, actual = getUseCases().achievement.getAchievements().sortedBy { it.id })
    }

    @Test
    fun updateAchievements() = runUnitTest {
        getUseCases().achievement.prepopulateAchievements()
        val gameSession = getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.getGameSessionPrototype())
        val newGameSession = gameSession.copy(
            currentStellarHostId = SUN,
            settledPlanetId = "3earth",
            finalHabitability = 0.9
        )
        assertTrue(actual = getUseCases().achievement.updateAchievements(gameSession = newGameSession).isNotEmpty())
    }
}
