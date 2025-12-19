package com.hybris.tlv.usecase.achievement

import kotlin.test.Test
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase

internal class AchievementUseCasesTest: TestCase() {

    @Test
    fun `sync and get achievements`() = runUnitTest {
        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
        useCases.achievement.syncAchievements()
        assertTrue(actual = useCases.achievement.getAchievements().isNotEmpty())
    }
}
