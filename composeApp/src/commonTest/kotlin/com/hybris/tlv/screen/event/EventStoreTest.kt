package com.hybris.tlv.screen.event

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.events
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.navigation.Screen

internal class EventStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getEventStore(ship = null)
        assertNotNull(actual = store.gameSession)
        val events = store.eventChain
        assertTrue(actual = events.isNotEmpty())
        val event = events.first { it.parentId == null && it.id != stopEvent.id }
        assertEquals(expected = event, actual = store.state.parentEvent)
        assertEquals(expected = listOf(element = stopEvent), actual = store.state.childrenEvents)
    }

    @Test
    fun initWithoutData() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        storeFactory.getEventStore(ship = null)
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun select() = runUnitTest {
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getEventStore(ship = null)
        val event = events.random()
        store.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = store.state.parentEvent)
    }

    @Test
    fun selectStopEvent() = runUnitTest {
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getEventStore(ship = null)
        store.send(action = EventAction.Select(event = stopEvent))
        assertNavigationBackstack(list = listOf(element = Screen.Game()))
    }

    @Test
    fun selectWithoutData() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        val store = storeFactory.getEventStore(ship = null)
        store.send(action = EventAction.Select(event = events.random()))
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        navigate(screen = Screen.Event())
        assertNavigationBackstack(list = listOf(element = Screen.Event()))
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        storeFactory.getEventStore(ship = null).back()
        assertNavigationBackstack(list = listOf(element = Screen.Event()))
    }
}
