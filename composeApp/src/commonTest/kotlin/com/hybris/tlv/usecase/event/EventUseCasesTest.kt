package com.hybris.tlv.usecase.event

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.events
import com.hybris.tlv.testCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventUseCasesTest {

    @BeforeTest
    fun setup() {
        testCore.sqlDriver.clearDatabase()
        testCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get events`() = runBlocking {
        assertTrue(actual = testCore.useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        testCore.useCases.event.syncEvents()
        assertTrue(actual = testCore.useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = testCore.useCases.event.getRandomEvent(ids = ids).isEmpty())
        assertEquals(
            expected = listOf(events.first()),
            actual = testCore.useCases.event.getRandomEvent(ids = ids - events.first().id)
        )
    }

    @Test
    fun `prepopulate and get events`() = runBlocking {
        assertTrue(actual = testCore.useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        testCore.useCases.event.prepopulateEvents()
        assertTrue(actual = testCore.useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = testCore.useCases.event.getRandomEvent(ids = ids).isEmpty())
        assertEquals(
            expected = listOf(events.first()),
            actual = testCore.useCases.event.getRandomEvent(ids = ids - events.first().id)
        )
    }
}
