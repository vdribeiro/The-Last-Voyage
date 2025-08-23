package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.gameSessionPrototype
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EventStoreTest {

    private val mock = Mock()
    private val store
        get() = EventStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = EventState(),
            eventUseCases = mock.useCases.event,
            gameSessionUseCases = mock.useCases.gameSession
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.EVENT)
    }

    @Test
    fun `init`() = runBlocking {
        mock.internalEvent.syncEvents()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        assertNotNull(actual = eventStore.stateFlow.value.gameSession)
        val events = eventStore.stateFlow.value.events
        assertTrue(actual = events.isNotEmpty())
        val event = events.find { it.parentId == null }
        assertEquals(expected = event, actual = eventStore.stateFlow.value.event)
        assertEquals(expected = events.filter { it.parentId == event?.id }, actual = eventStore.stateFlow.value.children)
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.EVENT, expected = mock.navigation.stateFlow.value.screen)
        val eventStore = store
        assertNull(actual = eventStore.stateFlow.value.gameSession)
        assertEquals(actual = NavigationManager.Screen.ERROR, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `init without events`() = runBlocking {
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        assertNotNull(actual = eventStore.stateFlow.value.gameSession)
        assertTrue(actual = eventStore.stateFlow.value.events.isNotEmpty())
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.internalEvent.syncEvents()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        assertEquals(actual = NavigationManager.Screen.EVENT, expected = mock.navigation.stateFlow.value.screen)
        eventStore.send(action = EventAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select`() = runBlocking {
        mock.internalEvent.syncEvents()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = eventStore.stateFlow.value.event)
    }

    @Test
    fun `send action select without game session`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.EVENT, expected = mock.navigation.stateFlow.value.screen)
        val eventStore = store
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(actual = NavigationManager.Screen.ERROR, expected = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select without selected event`() = runBlocking {
        assertEquals(actual = NavigationManager.Screen.EVENT, expected = mock.navigation.stateFlow.value.screen)
        mock.internalEvent.syncEvents()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store
        eventStore.send(action = EventAction.Select(event = null))
        3
    }
}
