package com.hybris.tlv.usecase.event

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.events
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset

internal class EventUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get events`() = runBlocking {
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isEmpty())
        getUseCases().event.syncEvents()
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = getUseCases().event.getRandomEvent(ids = ids).isEmpty())
        val event = events.first { it.parentId == null }
        assertEquals(
            expected = listOf(element = event),
            actual = getUseCases().event.getRandomEvent(ids = ids - event.id).filter { it.parentId == null }
        )
    }
}
