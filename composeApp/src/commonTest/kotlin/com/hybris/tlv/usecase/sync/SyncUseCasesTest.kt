package com.hybris.tlv.usecase.sync

import kotlin.test.Test
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.achievements
import com.hybris.tlv.catastrophes
import com.hybris.tlv.configs
import com.hybris.tlv.telemetry.Telemetry

internal class SyncUseCasesTest: TestCase() {

    @Test
    fun `sync and reset`() = runUnitTest {
        useCases.sync.sync()
//        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
//        useCases.achievement.syncAchievements()
//        assertTrue(actual = useCases.achievement.getAchievements().isNotEmpty())
    }
}
