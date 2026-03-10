package com.hybris.tlv.ui.screen.event

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class EventStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.event.prepopulateEvents()
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = storeFactory.get().getEventStore(ship = null)
        assertNotNull(actual = store.gameSession)
        assertTrue(actual = store.eventChain.isNotEmpty())
        assertFalse(actual = store.state.loading)
        assertNotNull(actual = store.state.ship)
        assertEquals(expected = store.eventChain.first { it.parentId == null && it.id != stopEvent.id }, actual = store.state.parentEvent)
        assertTrue(actual = store.state.childrenEvents.isNotEmpty())
    }

    @Test
    fun initWithoutData() = runUnitTest {
        assertNavigation(list = emptyList())
        storeFactory.get().getEventStore(ship = null)
        assertNavigation(list = listOf(Screen.Feedback(tag = null, message = null)))
    }

    @Test
    fun select() = runUnitTest {
        dependency.get().useCases.event.prepopulateEvents()
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = storeFactory.get().getEventStore(ship = null)
        val event = FakeData.events.get().random()
        store.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = store.state.parentEvent)
    }

    @Test
    fun selectStopEvent() = runUnitTest {
        dependency.get().useCases.event.prepopulateEvents()
        dependency.get().useCases.ship.prepopulateEngines()
        dependency.get().useCases.gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = storeFactory.get().getEventStore(ship = null)
        store.send(action = EventAction.Select(event = stopEvent))
        assertNavigation(list = listOf(Screen.Game(ship = null)))
    }

    @Test
    fun selectWithoutData() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = storeFactory.get().getEventStore(ship = null)
        store.send(action = EventAction.Select(event = FakeData.events.get().random()))
        assertNavigation(list = listOf(Screen.Feedback(tag = null, message = null)))
    }
}
