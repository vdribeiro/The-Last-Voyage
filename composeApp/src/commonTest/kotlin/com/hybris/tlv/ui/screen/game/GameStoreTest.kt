package com.hybris.tlv.ui.screen.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.hybris.tlv.TestCase
import com.hybris.tlv.gameSessionPrototype
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts

internal class GameStoreTest: TestCase() {

    @Test
    fun init() = TestCase.runUnitTest {
        TestCase.useCases.space.prepopulateStellarHosts()
        TestCase.useCases.space.prepopulatePlanets()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getGameStore(ship = null)
        assertNotNull(actual = store.gameSession)
        assertFalse(actual = store.state.loading)
        assertEquals(expected = Content.SYSTEM, actual = store.state.currentContent)
        assertNotNull(actual = store.state.ship)
        assertEquals(expected = stellarHosts.first(), actual = store.state.currentStellarHost)
        assertTrue(actual = store.state.nearStellarHosts.isNotEmpty())
    }

    @Test
    fun initWithoutGameSession() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.storeFactory.getGameStore(ship = null)
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun initWithoutStellarHost() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        TestCase.storeFactory.getGameStore(ship = null)
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun shipIsRepaired() = TestCase.runUnitTest {
        TestCase.useCases.space.prepopulateStellarHosts()
        TestCase.useCases.space.prepopulatePlanets()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = TestCase.useCases.gameSession.getLatestGameSession()!!
        TestCase.useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0)))
        val store = TestCase.storeFactory.getGameStore(ship = null)
        assertEquals(expected = 1, actual = store.state.ship?.integrity)
        assertEquals(expected = 89, actual = store.state.ship?.materials)
    }

    @Test
    fun gameOverByIntegrity() = TestCase.runUnitTest {
        TestCase.useCases.space.prepopulateStellarHosts()
        TestCase.useCases.space.prepopulatePlanets()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = TestCase.useCases.gameSession.getLatestGameSession()!!
        TestCase.useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0, materials = 0)))
        TestCase.assertNavigation(list = emptyList())
        TestCase.storeFactory.getGameStore(ship = null)
        TestCase.assertNavigation(list = listOf(Screen.GameOver))

    }

    @Test
    fun gameOverByFuel() = TestCase.runUnitTest {
        TestCase.useCases.space.prepopulateStellarHosts()
        TestCase.useCases.space.prepopulatePlanets()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val gameSession = TestCase.useCases.gameSession.getLatestGameSession()!!
        TestCase.useCases.gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(fuel = 0)))
        TestCase.assertNavigation(list = emptyList())
        TestCase.storeFactory.getGameStore(ship = null)
        TestCase.assertNavigation(list = listOf(Screen.GameOver))
    }

    @Test
    fun changeTab() = TestCase.runUnitTest {
        TestCase.useCases.space.prepopulateStellarHosts()
        TestCase.useCases.space.prepopulatePlanets()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getGameStore(ship = null)

        store.send(action = GameAction.ChangeTab(Content.SYSTEM))
        assertEquals(expected = Content.SYSTEM, actual = store.state.currentContent)

        store.send(action = GameAction.ChangeTab(Content.SHIP))
        assertEquals(expected = Content.SHIP, actual = store.state.currentContent)

        store.send(action = GameAction.ChangeTab(Content.TRAVEL))
        assertEquals(expected = Content.TRAVEL, actual = store.state.currentContent)
    }

    @Test
    fun travel() = TestCase.runUnitTest {
        TestCase.useCases.space.prepopulateStellarHosts()
        TestCase.useCases.space.prepopulatePlanets()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getGameStore(ship = null)

        TestCase.assertNavigation(list = emptyList())
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        TestCase.assertNavigation(list = listOf(Screen.Event()))
    }

    @Test
    fun travelWithoutGameSession() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        val store = TestCase.storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun travelWithoutStellarHost() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Travel(stellarHost = stellarHosts.first { it.id == "proxima_cen" }))
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun settle() = TestCase.runUnitTest {
        TestCase.useCases.space.prepopulateStellarHosts()
        TestCase.useCases.space.prepopulatePlanets()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        val store = TestCase.storeFactory.getGameStore(ship = null)

        TestCase.assertNavigation(list = emptyList())
        store.send(action = GameAction.Settle(planet = planets.first()))
        TestCase.assertNavigation(list = listOf(Screen.GameOver))
    }

    @Test
    fun settleWithoutGameSession() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        val store = TestCase.storeFactory.getGameStore(ship = null)
        store.send(action = GameAction.Settle(planet = planets.first()))
        TestCase.assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun navigateBack() = TestCase.runUnitTest {
        TestCase.assertNavigation(list = emptyList())
        TestCase.navigate(screen = Screen.Game())
        TestCase.assertNavigation(list = listOf(Screen.Game()))
        TestCase.useCases.space.prepopulateStellarHosts()
        TestCase.useCases.space.prepopulatePlanets()
        TestCase.useCases.ship.prepopulateEngines()
        TestCase.useCases.gameSession.startGame(gameSessionPrototype = gameSessionPrototype)
        TestCase.storeFactory.getGameStore(ship = null).back()
        TestCase.assertNavigation(list = listOf(Screen.Game(), Screen.MainMenu))
    }
}
