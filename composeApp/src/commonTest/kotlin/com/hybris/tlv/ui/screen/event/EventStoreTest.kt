package com.hybris.tlv.ui.screen.event

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.events
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.ui.navigation.Screen

internal class EventStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.event.prepopulateEvents()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getEventStore(ship = null)
        assertNotNull(actual = store.gameSession)
        assertTrue(actual = store.eventChain.isNotEmpty())
        assertFalse(actual = store.state.loading)
        assertNotNull(actual = store.state.ship)
        assertEquals(expected = store.eventChain.first { it.parentId == null && it.id != stopEvent.id }, actual = store.state.parentEvent)
        assertTrue(actual = store.state.childrenEvents.isNotEmpty())
    }

    @Test
    fun initWithoutData() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.storeFactory.getEventStore(ship = null)
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun select() = TestCase.runUnitTest {
        TestCase.useCases.event.prepopulateEvents()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getEventStore(ship = null)
        val event = events.random()
        store.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = store.state.parentEvent)
    }

    @Test
    fun selectStopEvent() = TestCase.runUnitTest {
        TestCase.useCases.event.prepopulateEvents()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getEventStore(ship = null)
        store.send(action = EventAction.Select(event = stopEvent))
        TestCase.assertNavigation(list = listOf(Screen.Game()))
    }

    @Test
    fun selectWithoutData() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        val store = TestCase.storeFactory.getEventStore(ship = null)
        store.send(action = EventAction.Select(event = events.random()))
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Event())
        TestCase.assertNavigation(list = listOf(Screen.Event()))
        TestCase.useCases.event.prepopulateEvents()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        TestCase.storeFactory.getEventStore(ship = null).back()
        TestCase.assertNavigation(list = listOf(Screen.Event()))
    }
}
