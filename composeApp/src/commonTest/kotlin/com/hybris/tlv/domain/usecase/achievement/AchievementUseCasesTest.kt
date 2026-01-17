package com.hybris.tlv.domain.usecase.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements
import com.hybris.tlv.domain.usecase.space.SUN
import com.hybris.tlv.gameSessionPrototype

internal class AchievementUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncAchievements() = runUnitTest {
        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
        useCases.achievement.prepopulateAchievements()
        assertEquals(expected = achievements.sortedBy { it.id }, actual = useCases.achievement.getAchievements().sortedBy { it.id })

        reset()
        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
        useCases.achievement.syncAchievements()
        assertEquals(expected = achievements.sortedBy { it.id }, actual = useCases.achievement.getAchievements().sortedBy { it.id })
    }

    @Test
    fun updateAchievements() = runUnitTest {
        useCases.achievement.prepopulateAchievements()
        val gameSession = useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val newGameSession = gameSession.copy(
            currentStellarHostId = SUN,
            settledPlanetId = "3earth",
            finalHabitability = 0.9
        )
        assertTrue(actual = useCases.achievement.updateAchievements(gameSession = newGameSession).isNotEmpty())
    }
}
