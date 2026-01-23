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
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = FakeData.getGameSessionPrototype())
        val store = storeFactory.getEventStore(ship = null)
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
        storeFactory.getEventStore(ship = null)
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun select() = runUnitTest {
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = FakeData.getGameSessionPrototype())
        val store = storeFactory.getEventStore(ship = null)
        val event = FakeData.getEvents().random()
        store.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = store.state.parentEvent)
    }

    @Test
    fun selectStopEvent() = runUnitTest {
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = FakeData.getGameSessionPrototype())
        val store = storeFactory.getEventStore(ship = null)
        store.send(action = EventAction.Select(event = stopEvent))
        assertNavigation(list = listOf(Screen.Game()))
    }

    @Test
    fun selectWithoutData() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = storeFactory.getEventStore(ship = null)
        store.send(action = EventAction.Select(event = FakeData.getEvents().random()))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Event())
        assertNavigation(list = listOf(Screen.Event()))
        useCases.event.prepopulateEvents()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = FakeData.getGameSessionPrototype())
        storeFactory.getEventStore(ship = null).back()
        assertNavigation(list = listOf(Screen.Event()))
    }
}
