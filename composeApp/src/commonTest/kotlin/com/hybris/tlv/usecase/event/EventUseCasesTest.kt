package com.hybris.tlv.usecase.event

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.events
import com.hybris.tlv.testDependency
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get events`() = runBlocking {
        assertTrue(actual = testDependency.useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        testDependency.useCases.event.syncEvents()
        assertTrue(actual = testDependency.useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = testDependency.useCases.event.getRandomEvent(ids = ids).isEmpty())
        val event = events.first { it.parentId == null }
        assertEquals(
            expected = listOf(element = event),
            actual = testDependency.useCases.event.getRandomEvent(ids = ids - event.id).filter { it.parentId == null }
        )
    }
}
