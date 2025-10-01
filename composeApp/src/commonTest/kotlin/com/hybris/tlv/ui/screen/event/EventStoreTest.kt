package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.events
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventStoreTest {

    private val store: EventStore get() = storeFactory.createEventStore()

    @BeforeTest
    fun setup() = runBlocking {
        testDependency.sqlDriver.clearDatabase()
        testDependency.navigation.navigate(screen = NavigationManager.Screen.Event)
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.event.prepopulateEvents()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        assertNotNull(actual = eventStore.gameSession)
        val events = eventStore.eventChain
        assertTrue(actual = events.isNotEmpty())
        val event = events.find { it.parentId == null }
        assertEquals(expected = event, actual = eventStore.stateFlow.value.parentEvent)
        assertEquals(expected = listOf(stopEvent), actual = eventStore.stateFlow.value.childrenEvents)
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
        val eventStore = store
        assertNull(actual = eventStore.stateFlow.value.ship)
        assertEquals(expected = NavigationManager.Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `init without events`() = runBlocking {
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        assertNotNull(actual = eventStore.stateFlow.value.ship)
        assertEquals(expected = defaultEvent, actual = eventStore.stateFlow.value.parentEvent)
    }

    @Test
    fun `send action back`() = runBlocking {
        testDependency.useCases.event.prepopulateEvents()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = NavigationManager.Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = NavigationManager.Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select`() = runBlocking {
        testDependency.useCases.event.prepopulateEvents()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = eventStore.stateFlow.value.parentEvent)
    }

    @Test
    fun `send action select without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = NavigationManager.Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select without selected event`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.event.prepopulateEvents()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        eventStore.send(action = EventAction.Select(event = defaultEvent))
    }
}
