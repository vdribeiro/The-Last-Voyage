package com.hybris.tlv.screen.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
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
        assertFalse(actual = store.state.loading)
        assertEquals(expected = Content.SYSTEM, actual = store.state.currentContent)
        assertNotNull(actual = store.state.ship)
        assertEquals(expected = stellarHosts.first(), actual = store.state.currentStellarHost)
        assertTrue(actual = store.state.nearStellarHosts.isNotEmpty())
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        storeFactory.getGameStore(ship = null)
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun initWithoutStellarHost() = runUnitTest {
        assertNavigation(list = emptyList())
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        storeFactory.getGameStore(ship = null)
        assertNavigation(list = listOf(Screen.Feedback()))
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
        assertNavigation(list = emptyList())
        storeFactory.getGameStore(ship = null)
        assertNavigation(list = listOf(Screen.GameOver))

    }

    @Test
    fun gameOverByFuel() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = useCases.gameSession.getLatestGameSession()!!
        useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(fuel = 0)))
        assertNavigation(list = emptyList())
        storeFactory.getGameStore(ship = null)
        assertNavigation(list = listOf(Screen.GameOver))
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

        assertNavigation(list = emptyList())
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        assertNavigation(list = listOf(Screen.Event()))
    }

    @Test
    fun travelWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun travelWithoutStellarHost() = runUnitTest {
        assertNavigation(list = emptyList())
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun settle() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = storeFactory.getGameStore(ship = null)

        assertNavigation(list = emptyList())
        store.send(action = GameAction.Settle(planet = planets.first()))
        assertNavigation(list = listOf(Screen.GameOver))
    }

    @Test
    fun settleWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Settle(planet = planets.first()))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Game())
        assertNavigation(list = listOf(Screen.Game()))
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        useCases.ship.prepopulateEngines()
        useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        storeFactory.getGameStore(ship = null).back()
        assertNavigation(list = listOf(Screen.Game(), Screen.MainMenu))
    }
}
