package com.hybris.tlv.usecase.event

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.events
import com.hybris.tlv.reset
import com.hybris.tlv.useCases

internal class EventUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get events`() = runBlocking {
        assertTrue(actual = useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        useCases.event.syncEvents()
        assertTrue(actual = useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = useCases.event.getRandomEvent(ids = ids).isEmpty())
        val event = events.first { it.parentId == null }
        assertEquals(
            expected = listOf(element = event),
            actual = useCases.event.getRandomEvent(ids = ids - event.id).filter { it.parentId == null }
        )
    }
}
