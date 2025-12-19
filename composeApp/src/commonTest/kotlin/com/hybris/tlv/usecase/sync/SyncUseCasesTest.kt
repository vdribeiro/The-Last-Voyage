package com.hybris.tlv.usecase.sync

import kotlin.test.Test
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase

internal class SyncUseCasesTest: TestCase() {

    @Test
    fun `sync and reset`() = runUnitTest {
        useCases.sync.sync()
//        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
//        useCases.achievement.syncAchievements()
//        assertTrue(actual = useCases.achievement.getAchievements().isNotEmpty())
    }
}
