package com.hybris.tlv.domain.usecase.sync

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import com.hybris.tlv.TestCase
import com.hybris.tlv.domain.flag.FeatureFlags
import com.hybris.tlv.domain.usecase.sync.model.DataSource
import com.hybris.tlv.domain.usecase.sync.model.SyncResult

internal class SyncUseCasesTest: TestCase() {

    @Test
    fun syncAndReset() = runUnitTest {
        assertTrue(actual = useCases.translation.getTranslations().isEmpty())
        assertNull(actual = useCases.catastrophe.getRandomCatastrophe())
        assertTrue(actual = useCases.ship.getEngines().isEmpty())
        assertTrue(actual = useCases.space.observeExoplanets().first().isEmpty())
        assertTrue(actual = useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
        assertTrue(actual = useCases.credit.getCredits().isEmpty())
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
            ), actual = useCases.sync.sync(reset = true)
        )
        assertFalse(actual = useCases.translation.getTranslations().isEmpty())
        assertNotNull(actual = useCases.catastrophe.getRandomCatastrophe())
        assertFalse(actual = useCases.ship.getEngines().isEmpty())
        assertFalse(actual = useCases.space.observeExoplanets().first().isEmpty())
        assertFalse(actual = useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        assertFalse(actual = useCases.achievement.getAchievements().isEmpty())
        assertFalse(actual = useCases.credit.getCredits().isEmpty())

        reset()
        assertTrue(actual = useCases.translation.getTranslations().isEmpty())
        assertNull(actual = useCases.catastrophe.getRandomCatastrophe())
        assertTrue(actual = useCases.ship.getEngines().isEmpty())
        assertTrue(actual = useCases.space.observeExoplanets().first().isEmpty())
        assertTrue(actual = useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        assertTrue(actual = useCases.achievement.getAchievements().isEmpty())
        assertTrue(actual = useCases.credit.getCredits().isEmpty())
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
            ), actual = useCases.sync.sync(reset = true)
        )
        assertFalse(actual = useCases.translation.getTranslations().isEmpty())
        assertNotNull(actual = useCases.catastrophe.getRandomCatastrophe())
        assertFalse(actual = useCases.ship.getEngines().isEmpty())
        assertFalse(actual = useCases.space.observeExoplanets().first().isEmpty())
        assertFalse(actual = useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        assertFalse(actual = useCases.achievement.getAchievements().isEmpty())
        assertFalse(actual = useCases.credit.getCredits().isEmpty())
    }
}
