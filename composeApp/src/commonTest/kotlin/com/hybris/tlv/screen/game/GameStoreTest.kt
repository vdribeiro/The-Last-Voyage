package com.hybris.tlv.screen.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts

internal class GameStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameStore(ship = null)
        assertNotNull(actual = store.gameSession)
        assertNotNull(actual = store.state.ship)
        assertEquals(expected = Content.SYSTEM, actual = store.state.currentContent)
        assertEquals(expected = stellarHosts.first(), actual = store.state.currentStellarHost)
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        storeFactory.getGameStore(ship = null)
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun initWithoutStellarHost() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        storeFactory.getGameStore(ship = null)
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun shipIsRepaired() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0)))
        val store = storeFactory.getGameStore(ship = null)
        assertEquals(expected = 1, actual = store.state.ship?.integrity)
        assertEquals(expected = 89, actual = store.state.ship?.materials)
    }

    @Test
    fun gameOverByIntegrity() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0, materials = 0)))
        assertNavigationBackstack(list = emptyList())
        storeFactory.getGameStore(ship = null)
        assertNavigationBackstack(list = listOf(element = Screen.GameOver))

    }

    @Test
    fun gameOverByFuel() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(fuel = 0)))
        assertNavigationBackstack(list = emptyList())
        storeFactory.getGameStore(ship = null)
        assertNavigationBackstack(list = listOf(element = Screen.GameOver))
    }

    @Test
    fun changeTab() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameStore(ship = null)

        store.send(action = GameAction.ChangeTab(Content.SYSTEM))
        assertEquals(expected = Content.SYSTEM, actual = store.state.currentContent)

        store.send(action = GameAction.ChangeTab(Content.SHIP))
        assertEquals(expected = Content.SHIP, actual = store.state.currentContent)

        store.send(action = GameAction.ChangeTab(Content.TRAVEL))
        assertEquals(expected = Content.TRAVEL, actual = store.state.currentContent)
    }

    @Test
    fun travel() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameStore(ship = null)

        assertNavigationBackstack(list = emptyList())
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        assertNavigationBackstack(list = listOf(element = Screen.Event()))
    }

    @Test
    fun travelWithoutGameSession() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        val store = storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun travelWithoutStellarHost() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun settle() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameStore(ship = null)

        assertNavigationBackstack(list = emptyList())
        store.send(action = GameAction.Settle(planet = planets.first()))
        assertNavigationBackstack(list = listOf(element = Screen.GameOver))
    }

    @Test
    fun settleWithoutGameSession() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        val store = storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Settle(planet = planets.first()))
        assertNavigationBackstack(list = listOf(element = Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigationBackstack(list = emptyList())
        navigate(screen = Screen.Game())
        assertNavigationBackstack(list = listOf(element = Screen.Game()))
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        storeFactory.getGameStore(ship = null).back()
        assertNavigationBackstack(list = listOf(Screen.Game(), Screen.MainMenu))
    }
}
