package com.hybris.tlv.screen.event

import kotlin.collections.orEmpty
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.events
import com.hybris.tlv.getEventStore
import com.hybris.tlv.reset

internal class EventStoreTest {

    private val store: EventStore get() = getEventStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = EventScreen))
    }

    @Test
    fun `init`() = runBlocking {
//        useCases.event.syncEvents()
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        assertNotNull(actual = eventStore.gameSession)
        val events = eventStore.eventChain
        assertTrue(actual = events.orEmpty().isNotEmpty())
        val event = events.orEmpty().find { it.parentId == null }
        assertEquals(expected = event, actual = eventStore.stateFlow.value.parentEvent)
        assertEquals(expected = listOf(stopEvent), actual = eventStore.stateFlow.value.childrenEvents)
    }

    @Test
    fun `init without game session`() = runBlocking {
//        assertEquals(expected = EventScreen, actual = getNavigation().stateFlow.value.screen)
        val eventStore = store
        assertNull(actual = eventStore.stateFlow.value.ship)
//        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
    }

    @Test
    fun `init without events`() = runBlocking {
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        assertNotNull(actual = eventStore.stateFlow.value.ship)
        assertEquals(expected = defaultEvent, actual = eventStore.stateFlow.value.parentEvent)
    }

    @Test
    fun `send action back`() = runBlocking {
//        useCases.event.syncEvents()
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
//        assertEquals(expected = EventScreen, actual = getNavigation().stateFlow.value.screen)
//        getNavigation().back()
//        assertEquals(expected = EventScreen, actual = getNavigation().stateFlow.value.screen)
    }

    @Test
    fun `send action select`() = runBlocking {
//        useCases.event.syncEvents()
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = eventStore.stateFlow.value.parentEvent)
    }

    @Test
    fun `send action select without game session`() = runBlocking {
//        assertEquals(expected = EventScreen, actual = getNavigation().stateFlow.value.screen)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
//        assertEquals(expected = Screen.Feedback, actual = getNavigation().stateFlow.value.screen)
    }

    @Test
    fun `send action select without selected event`() = runBlocking {
//        assertEquals(expected = EventScreen, actual = getNavigation().stateFlow.value.screen)
//        useCases.event.syncEvents()
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        eventStore.send(action = EventAction.Select(event = defaultEvent))
    }
}
