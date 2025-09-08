package com.hybris.tlv.usecase.event

import com.hybris.tlv.Core
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.TestDispatchers
import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.mock.events
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class EventUseCasesTest {

    private val mock by lazy {
        Core(
            dispatcher = TestDispatchers(),
            sqlDriver = createSqlDriver(inMemory = true),
            httpClient = HttpClientFactory.buildHttpClient()
        )
    }

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `sync and get events`() = runBlocking {
        assertTrue(actual = mock.useCases.event.getRandomEvent(ids = emptySet()).isEmpty())
        mock.useCases.sync.sync().last()
        assertTrue(actual = mock.useCases.event.getRandomEvent(ids = emptySet()).isNotEmpty())
        val ids = events.map { it.id }.toSet()
        assertTrue(actual = mock.useCases.event.getRandomEvent(ids = ids).isEmpty())
        assertEquals(
            expected = listOf(events.first()),
            actual = mock.useCases.event.getRandomEvent(ids = ids - events.first().id)
        )
    }
}
