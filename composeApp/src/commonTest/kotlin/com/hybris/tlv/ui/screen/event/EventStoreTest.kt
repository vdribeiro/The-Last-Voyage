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
import com.hybris.tlv.reset
import com.hybris.tlv.testDependency
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.Screen

internal class EventStoreTest {

    private val store: EventStore get() = testDependency.storeFactory.createEventStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
        testDependency.navigation.navigate(navigationState = NavigationState(screen = Screen.Event))
    }

    @Test
    fun `init`() = runBlocking {
        testDependency.useCases.event.syncEvents()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
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
        assertEquals(expected = Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
        val eventStore = store
        assertNull(actual = eventStore.stateFlow.value.ship)
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
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
        testDependency.useCases.event.syncEvents()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store
        assertEquals(expected = Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.navigation.back()
        assertEquals(expected = Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select`() = runBlocking {
        testDependency.useCases.event.syncEvents()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = eventStore.stateFlow.value.parentEvent)
    }

    @Test
    fun `send action select without game session`() = runBlocking {
        assertEquals(expected = Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = Screen.Feedback, actual = testDependency.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select without selected event`() = runBlocking {
        assertEquals(expected = Screen.Event, actual = testDependency.navigation.stateFlow.value.screen)
        testDependency.useCases.event.syncEvents()
        testDependency.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        eventStore.send(action = EventAction.Select(event = defaultEvent))
    }
}
