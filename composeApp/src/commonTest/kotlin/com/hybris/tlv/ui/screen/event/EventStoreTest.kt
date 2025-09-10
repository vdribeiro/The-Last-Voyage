package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.events
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.mock
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class EventStoreTest {

    private val store by lazy {
        EventStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = EventState(),
            eventUseCases = mock.useCases.event,
            gameSessionUseCases = mock.useCases.gameSession
        )
    }

    @BeforeTest
    fun setup() = runBlocking {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.EVENT)
    }

    @Test
    fun `init`() = runBlocking {
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store.apply { setup(state = EventState()) }
        assertNotNull(actual = eventStore.stateFlow.value.gameSession)
        val events = eventStore.stateFlow.value.events.orEmpty()
        assertTrue(actual = events.isNotEmpty())
        val event = events.find { it.parentId == null }
        assertEquals(expected = event, actual = eventStore.stateFlow.value.event)
        assertEquals(expected = events.filter { it.parentId == event?.id }, actual = eventStore.stateFlow.value.children)
    }

    @Test
    fun `init without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = mock.navigation.stateFlow.value.screen)
        val eventStore = store.apply { setup(state = EventState()) }
        assertNull(actual = eventStore.stateFlow.value.gameSession)
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `init without events`() = runBlocking {
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store.apply { setup(state = EventState()) }
        assertNotNull(actual = eventStore.stateFlow.value.gameSession)
        assertTrue(actual = eventStore.stateFlow.value.events.orEmpty().isNotEmpty())
    }

    @Test
    fun `send action back`() = runBlocking {
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        store.setup(state = EventState())
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = mock.navigation.stateFlow.value.screen)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.GAME, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select`() = runBlocking {
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store.apply { setup(state = EventState()) }
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = event, actual = eventStore.stateFlow.value.event)
    }

    @Test
    fun `send action select without game session`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = mock.navigation.stateFlow.value.screen)
        val eventStore = store.apply { setup(state = EventState()) }
        val event = events.random()
        eventStore.send(action = EventAction.Select(event = event))
        assertEquals(expected = NavigationManager.Screen.FEEDBACK, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action select without selected event`() = runBlocking {
        assertEquals(expected = NavigationManager.Screen.EVENT, actual = mock.navigation.stateFlow.value.screen)
        mock.useCases.sync.sync().last()
        mock.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val eventStore = store.apply { setup(state = EventState()) }
        eventStore.send(action = EventAction.Select(event = null))
    }
}
