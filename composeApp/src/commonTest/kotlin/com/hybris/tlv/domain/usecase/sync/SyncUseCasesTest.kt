package com.hybris.tlv.domain.usecase.sync

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import com.hybris.tlv.domain.flag.FeatureFlags
import com.hybris.tlv.domain.usecase.sync.model.DataSource
import com.hybris.tlv.domain.usecase.sync.model.SyncResult
import com.hybris.tlv.test.TestCase

internal class SyncUseCasesTest: TestCase() {

    @Test
    fun syncAndReset() = runUnitTest {
        assertEmpty()
        FeatureFlags.set { it.copy(http = false) }
        assertEquals(
            expected = SyncResult(
                translations = DataSource.LOCAL,
                catastrophes = DataSource.LOCAL,
                engines = DataSource.LOCAL,
                stellarHosts = DataSource.LOCAL,
                planets = DataSource.LOCAL,
                events = DataSource.LOCAL,
                achievements = DataSource.LOCAL,
                credits = DataSource.LOCAL
            ), actual = dependency.get().useCases.sync.sync(reset = true)
        )
        assertNotEmpty()

        resetData()

        assertEmpty()
        FeatureFlags.set { it.copy(http = true) }
        assertEquals(
            expected = SyncResult(
                translations = DataSource.REMOTE,
                catastrophes = DataSource.REMOTE,
                engines = DataSource.REMOTE,
                stellarHosts = DataSource.REMOTE,
                planets = DataSource.REMOTE,
                events = DataSource.REMOTE,
                achievements = DataSource.REMOTE,
                credits = DataSource.REMOTE
            ), actual = dependency.get().useCases.sync.sync(reset = true)
        )
        assertNotEmpty()
    }

    private suspend fun assertEmpty() {
        assertTrue(actual = dependency.get().useCases.translation.getTranslations().isEmpty())
        assertNull(actual = dependency.get().useCases.catastrophe.getCatastrophes().isEmpty())
        assertTrue(actual = dependency.get().useCases.ship.getEngines().isEmpty())
        assertTrue(actual = dependency.get().useCases.space.observeExoplanets().first().isEmpty())
        assertTrue(actual = dependency.get().useCases.event.observeEvents().first().isEmpty())
        assertTrue(actual = dependency.get().useCases.achievement.getAchievements().isEmpty())
        assertTrue(actual = dependency.get().useCases.credit.getCredits().isEmpty())
    }

    private suspend fun assertNotEmpty() {
        assertFalse(actual = dependency.get().useCases.translation.getTranslations().isEmpty())
        assertNotNull(actual = dependency.get().useCases.catastrophe.getCatastrophes().isEmpty())
        assertFalse(actual = dependency.get().useCases.ship.getEngines().isEmpty())
        assertFalse(actual = dependency.get().useCases.space.observeExoplanets().first().isEmpty())
        assertFalse(actual = dependency.get().useCases.event.observeEvents().first().isEmpty())
        assertFalse(actual = dependency.get().useCases.achievement.getAchievements().isEmpty())
        assertFalse(actual = dependency.get().useCases.credit.getCredits().isEmpty())
    }
}
