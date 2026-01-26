package com.hybris.tlv.ui.screen.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class GameStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = getStoreFactory().getGameStore(ship = null)
        assertNotNull(actual = store.gameSession)
        assertFalse(actual = store.state.loading)
        assertEquals(expected = Content.SYSTEM, actual = store.state.currentContent)
        assertNotNull(actual = store.state.ship)
        assertEquals(expected = FakeData.stellarHosts.get().first(), actual = store.state.currentStellarHost)
        assertTrue(actual = store.state.nearStellarHosts.isNotEmpty())
    }

    @Test
    fun initWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        getStoreFactory().getGameStore(ship = null)
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun initWithoutStellarHost() = runUnitTest {
        assertNavigation(list = emptyList())
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        getStoreFactory().getGameStore(ship = null)
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun shipIsRepaired() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val gameSession = getUseCases().gameSession.getLatestGameSession()!!
        getUseCases().gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0)))
        val store = getStoreFactory().getGameStore(ship = null)
        assertEquals(expected = 1, actual = store.state.ship?.integrity)
        assertEquals(expected = 89, actual = store.state.ship?.materials)
    }

    @Test
    fun gameOverByIntegrity() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val gameSession = getUseCases().gameSession.getLatestGameSession()!!
        getUseCases().gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(integrity = 0, materials = 0)))
        assertNavigation(list = emptyList())
        getStoreFactory().getGameStore(ship = null)
        assertNavigation(list = listOf(Screen.GameOver))

    }

    @Test
    fun gameOverByFuel() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val gameSession = getUseCases().gameSession.getLatestGameSession()!!
        getUseCases().gameSession.updateGameSession(gameSession = gameSession.copy(ship = gameSession.ship.copy(fuel = 0)))
        assertNavigation(list = emptyList())
        getStoreFactory().getGameStore(ship = null)
        assertNavigation(list = listOf(Screen.GameOver))
    }

    @Test
    fun changeTab() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = getStoreFactory().getGameStore(ship = null)

        store.send(action = GameAction.ChangeTab(Content.SYSTEM))
        assertEquals(expected = Content.SYSTEM, actual = store.state.currentContent)

        store.send(action = GameAction.ChangeTab(Content.SHIP))
        assertEquals(expected = Content.SHIP, actual = store.state.currentContent)

        store.send(action = GameAction.ChangeTab(Content.TRAVEL))
        assertEquals(expected = Content.TRAVEL, actual = store.state.currentContent)
    }

    @Test
    fun travel() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = getStoreFactory().getGameStore(ship = null)

        assertNavigation(list = emptyList())
        store.send(action = GameAction.Travel(stellarHost = FakeData.stellarHosts.get().first { it.id == "proxima_cen" }))
        assertNavigation(list = listOf(Screen.Event()))
    }

    @Test
    fun travelWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = getStoreFactory().getGameStore(ship = null)
        store.send(action = GameAction.Travel(stellarHost = FakeData.stellarHosts.get().first { it.id == "proxima_cen" }))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun travelWithoutStellarHost() = runUnitTest {
        assertNavigation(list = emptyList())
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = getStoreFactory().getGameStore(ship = null)
        store.send(action = GameAction.Travel(stellarHost = FakeData.stellarHosts.get().first { it.id == "proxima_cen" }))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun settle() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        val store = getStoreFactory().getGameStore(ship = null)

        assertNavigation(list = emptyList())
        store.send(action = GameAction.Settle(planet = FakeData.planets.get().first()))
        assertNavigation(list = listOf(Screen.GameOver))
    }

    @Test
    fun settleWithoutGameSession() = runUnitTest {
        assertNavigation(list = emptyList())
        val store = getStoreFactory().getGameStore(ship = null)
        store.send(action = GameAction.Settle(planet = FakeData.planets.get().first()))
        assertNavigation(list = listOf(Screen.Feedback()))
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.Game())
        assertNavigation(list = listOf(Screen.Game()))
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        getUseCases().ship.prepopulateEngines()
        getUseCases().gameSession.startGame(gameSessionPrototype = FakeData.gameSessionPrototype.get())
        getStoreFactory().getGameStore(ship = null).back()
        assertNavigation(list = listOf(Screen.Game(), Screen.MainMenu))
    }
}
