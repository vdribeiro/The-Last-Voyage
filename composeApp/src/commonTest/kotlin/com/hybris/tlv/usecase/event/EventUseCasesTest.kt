package com.hybris.tlv.usecase.event

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.events
import com.hybris.tlv.mockCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventUseCasesTest {

    @BeforeTest
    fun setup() {
        mockCore.sqlDriver.clearDatabase()
        mockCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get events`() = runBlocking {
        assertTrue(actual = mockCore.useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        mockCore.useCases.event.syncEvents()
        assertTrue(actual = mockCore.useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = mockCore.useCases.event.getRandomEvent(ids = ids).isEmpty())
        assertEquals(
            expected = listOf(events.first()),
            actual = mockCore.useCases.event.getRandomEvent(ids = ids - events.first().id)
        )
    }

    @Test
    fun `prepopulate and get events`() = runBlocking {
        assertTrue(actual = mockCore.useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        mockCore.useCases.event.prepopulateEvents()
        assertTrue(actual = mockCore.useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = mockCore.useCases.event.getRandomEvent(ids = ids).isEmpty())
        assertEquals(
            expected = listOf(events.first()),
            actual = mockCore.useCases.event.getRandomEvent(ids = ids - events.first().id)
        )
    }
}
