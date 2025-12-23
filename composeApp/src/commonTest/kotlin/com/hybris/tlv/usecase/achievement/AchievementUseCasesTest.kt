package com.hybris.tlv.usecase.achievement

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements

internal class AchievementUseCasesTest: TestCase() {

    @Test
    fun `prepopulate and sync achievements`() = runUnitTest {
        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
        useCases.achievement.prepopulateAchievements()
        assertEquals(expected = achievements.sortedBy { it.id }, actual = useCases.achievement.getAchievements().sortedBy { it.id })

        reset()
        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
        useCases.achievement.syncAchievements()
        assertEquals(expected = achievements.sortedBy { it.id }, actual = useCases.achievement.getAchievements().sortedBy { it.id })
    }

    @Test
    fun `update achievements`() = runUnitTest {
        useCases.achievement.prepopulateAchievements()

        // TODO
    }
}
