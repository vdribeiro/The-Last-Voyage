package com.hybris.tlv.usecase.sync

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.flag.FeatureFlags
import com.hybris.tlv.usecase.sync.model.DataSource
import com.hybris.tlv.usecase.sync.model.SyncResult

internal class SyncUseCasesTest: TestCase() {

    @Test
    fun syncAndReset() = runUnitTest {
        assertTrue(actual = useCases.sync.isEmpty())
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
        assertFalse(actual = useCases.sync.isEmpty())
        reset()
        assertTrue(actual = useCases.sync.isEmpty())
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
    }
}
