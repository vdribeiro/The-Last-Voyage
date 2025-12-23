package com.hybris.tlv.screen.event

import kotlin.test.Test
import com.hybris.tlv.TestCase
import com.hybris.tlv.navigation.Screen

internal class EventStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
//        useCases.event.prepopulateEvents()
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        val store = storeFactory.getEventStore(ship = null)
//        assertNotNull(actual = store.gameSession)
//        val events = store.eventChain
//        assertTrue(actual = events.isNotEmpty())
//        val event = events.find { it.parentId == null }
//        assertEquals(expected = event, actual = store.state().parentEvent)
//        assertEquals(expected = listOf(element = stopEvent), actual = store.state().childrenEvents)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        storeFactory.getEventStore(ship = null)
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

//    @Test
//    fun `init without events`() = runUnitTest {
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        val store = store
//        assertNotNull(actual = store.state().ship)
//        assertEquals(expected = defaultEvent, actual = store.state().parentEvent)
//    }
//
//    @Test
//    fun `send action back`() = runUnitTest {
//        useCases.event.syncEvents()
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        store
//        assertEquals(expected = EventScreen, actual = getNavigation().state().screen)
//        getNavigation().back()
//        assertEquals(expected = EventScreen, actual = getNavigation().state().screen)
//    }
//
//    @Test
//    fun `send action select`() = runUnitTest {
//        useCases.event.syncEvents()
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        val store = store
//        val event = events.random()
//        store.send(action = EventAction.Select(event = event))
//        assertEquals(expected = event, actual = store.state().parentEvent)
//    }
//
//    @Test
//    fun `send action select without game session`() = runUnitTest {
//        assertEquals(expected = EventScreen, actual = getNavigation().state().screen)
//        val store = store
//        val event = events.random()
//        store.send(action = EventAction.Select(event = event))
//        assertEquals(expected = Screen.Feedback, actual = getNavigation().state().screen)
//    }
//
//    @Test
//    fun `send action select without selected event`() = runUnitTest {
//        assertEquals(expected = EventScreen, actual = getNavigation().state().screen)
//        useCases.event.syncEvents()
//        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
//        val store = store
//        store.send(action = EventAction.Select(event = defaultEvent))
//    }
}
