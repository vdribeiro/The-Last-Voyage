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
        assertTrue(actual = getUseCases().translation.getTranslations().isEmpty())
        assertNull(actual = getUseCases().catastrophe.getRandomCatastrophe())
        assertTrue(actual = getUseCases().ship.getEngines().isEmpty())
        assertTrue(actual = getUseCases().space.observeExoplanets().first().isEmpty())
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isEmpty())
        assertTrue(actual = getUseCases().achievement.getAchievements().isEmpty())
        assertTrue(actual = getUseCases().credit.getCredits().isEmpty())
        FeatureFlags.set { it.copy(http = false) }
        assertEquals(
            expected = SyncResult(
                archive = DataSource.NONE,
                translations = DataSource.LOCAL,
                catastrophes = DataSource.LOCAL,
                engines = DataSource.LOCAL,
                stellarHosts = DataSource.LOCAL,
                planets = DataSource.LOCAL,
                events = DataSource.LOCAL,
                achievements = DataSource.LOCAL,
                credits = DataSource.LOCAL
            ), actual = getUseCases().sync.sync(reset = true)
        )
        assertFalse(actual = getUseCases().translation.getTranslations().isEmpty())
        assertNotNull(actual = getUseCases().catastrophe.getRandomCatastrophe())
        assertFalse(actual = getUseCases().ship.getEngines().isEmpty())
        assertFalse(actual = getUseCases().space.observeExoplanets().first().isEmpty())
        assertFalse(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isEmpty())
        assertFalse(actual = getUseCases().achievement.getAchievements().isEmpty())
        assertFalse(actual = getUseCases().credit.getCredits().isEmpty())

        reset()
        assertTrue(actual = getUseCases().translation.getTranslations().isEmpty())
        assertNull(actual = getUseCases().catastrophe.getRandomCatastrophe())
        assertTrue(actual = getUseCases().ship.getEngines().isEmpty())
        assertTrue(actual = getUseCases().space.observeExoplanets().first().isEmpty())
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isEmpty())
        assertTrue(actual = getUseCases().achievement.getAchievements().isEmpty())
        assertTrue(actual = getUseCases().credit.getCredits().isEmpty())
        FeatureFlags.set { it.copy(http = true) }
        assertEquals(
            expected = SyncResult(
                archive = DataSource.REMOTE,
                translations = DataSource.REMOTE,
                catastrophes = DataSource.REMOTE,
                engines = DataSource.REMOTE,
                stellarHosts = DataSource.REMOTE,
                planets = DataSource.REMOTE,
                events = DataSource.REMOTE,
                achievements = DataSource.REMOTE,
                credits = DataSource.REMOTE
            ), actual = getUseCases().sync.sync(reset = true)
        )
        assertFalse(actual = getUseCases().translation.getTranslations().isEmpty())
        assertNotNull(actual = getUseCases().catastrophe.getRandomCatastrophe())
        assertFalse(actual = getUseCases().ship.getEngines().isEmpty())
        assertFalse(actual = getUseCases().space.observeExoplanets().first().isEmpty())
        assertFalse(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isEmpty())
        assertFalse(actual = getUseCases().achievement.getAchievements().isEmpty())
        assertFalse(actual = getUseCases().credit.getCredits().isEmpty())
    }
}
