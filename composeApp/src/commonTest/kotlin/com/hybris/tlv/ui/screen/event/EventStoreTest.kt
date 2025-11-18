package com.hybris.tlv.ui.screen.event

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.events
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.getNavigation
import com.hybris.tlv.getStoreFactory
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Route

internal class EventStoreTest {

    private val store: EventStore get() = getStoreFactory().createEventStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        getNavigation().navigate(navigationState = NavigationState(route = Route.Event))
    }

    @Test
    fun `init`() = runBlocking {
        getUseCases().event.syncEvents()
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
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
        assertEquals(expected = Route.Event, actual = getNavigation().stateFlow.value.route)
        val eventStore = store
        assertNull(actual = eventStore.stateFlow.value.ship)
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `init without events`() = runBlocking {
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        assertNotNull(actual = eventStore.stateFlow.value.ship)
        assertEquals(expected = defaultEvent, actual = eventStore.stateFlow.value.parentEvent)
    }

    @Test
    fun `send action back`() = runBlocking {
        getUseCases().event.syncEvents()
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = Route.Event, actual = getNavigation().stateFlow.value.route)
        getNavigation().back()
        assertEquals(expected = Route.Event, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action select`() = runBlocking {
        getUseCases().event.syncEvents()
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = eventStore.stateFlow.value.parentEvent)
    }

    @Test
    fun `send action select without game session`() = runBlocking {
        assertEquals(expected = Route.Event, actual = getNavigation().stateFlow.value.route)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = Route.Feedback, actual = getNavigation().stateFlow.value.route)
    }

    @Test
    fun `send action select without selected event`() = runBlocking {
        assertEquals(expected = Route.Event, actual = getNavigation().stateFlow.value.route)
        getUseCases().event.syncEvents()
        getUseCases().gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        eventStore.send(action = EventAction.Select(event = defaultEvent))
    }
}
