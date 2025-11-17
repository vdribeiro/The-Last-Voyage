package com.hybris.tlv.usecase.achievement

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset

internal class AchievementUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get achievements`() = runBlocking {
        assertTrue(actual = getUseCases().achievement.getAchievements().isEmpty())
        getUseCases().achievement.syncAchievements()
        assertTrue(actual = getUseCases().achievement.getAchievements().isNotEmpty())
    }
}
