package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.ui.component.LazyListIndex
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class StellarExplorerStoreTest {

    private val mock = Mock()
    private val store
        get() = StellarExplorerStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = StellarExplorerState(),
            spaceUseCases = mock.useCases.space
        )

    @BeforeTest
    fun setup() = runBlocking {
        mock.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)
    }

    @Test
    fun `init`() = runBlocking {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        val stellarExplorerStore = store
        assertEquals(actual = Content.LIST_HOSTS, expected = stellarExplorerStore.stateFlow.value.currentContent)
        val stellarHosts = stellarExplorerStore.stateFlow.value.stellarHosts
        val planets = stellarExplorerStore.stateFlow.value.planets
        assertEquals(actual = stellarHosts, expected = stellarExplorerStore.stateFlow.value.filteredStellarHosts)
        assertEquals(actual = planets, expected = stellarExplorerStore.stateFlow.value.filteredPlanets)
    }

    @Test
    fun `send action back`() = runBlocking {
        val stellarExplorerStore = store
        assertEquals(actual = NavigationManager.Screen.STELLAR_EXPLORER, expected = mock.navigation.stateFlow.value.screen)
        assertEquals(actual = Content.LIST_HOSTS, expected = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)

        mock.navigation.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)
        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        assertEquals(actual = Content.LIST_PLANETS, expected = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.Back)
        assertEquals(actual = NavigationManager.Screen.MAIN_MENU, expected = mock.navigation.stateFlow.value.screen)

        stellarExplorerStore.send(action = StellarExplorerAction.OpenPlanet(planet = planets.first()))
        assertEquals(actual = Content.DETAIL_PLANETS, expected = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.Back)
        assertEquals(actual = Content.LIST_PLANETS, expected = stellarExplorerStore.stateFlow.value.currentContent)

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        stellarExplorerStore.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = stellarHosts.first()))
        assertEquals(actual = Content.DETAIL_HOSTS, expected = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.Back)
        assertEquals(actual = Content.LIST_HOSTS, expected = stellarExplorerStore.stateFlow.value.currentContent)
    }

    @Test
    fun `send action save index`() = runBlocking {
        val stellarExplorerStore = store
        assertEquals(actual = LazyListIndex(), expected = stellarExplorerStore.stateFlow.value.listIndex)
        stellarExplorerStore.send(action = StellarExplorerAction.SaveIndex(index = LazyListIndex(index = 6, scrollOffset = 9)))
        assertEquals(actual = LazyListIndex(index = 6, scrollOffset = 9), expected = stellarExplorerStore.stateFlow.value.listIndex)
    }

    @Test
    fun `send action search`() = runBlocking {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.Search(search = stellarHosts.first().id))
        assertEquals(actual = listOf(stellarHosts.first()), expected = stellarExplorerStore.stateFlow.value.filteredStellarHosts)

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        stellarExplorerStore.send(action = StellarExplorerAction.Search(search = planets.first().id))
        assertEquals(actual = listOf(planets.first()), expected = stellarExplorerStore.stateFlow.value.filteredPlanets)
    }

    @Test
    fun `send action sort`() = runBlocking {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.SortStellarHosts(sort = StellarHostProperty.NAME))
        assertEquals(actual = StellarHostProperty.NAME, expected = stellarExplorerStore.stateFlow.value.sortStellarHostProperty)

        stellarExplorerStore.send(action = StellarExplorerAction.SortPlanets(sort = PlanetProperty.NAME))
        assertEquals(actual = PlanetProperty.NAME, expected = stellarExplorerStore.stateFlow.value.sortPlanetProperty)

        assertEquals(actual = true, expected = stellarExplorerStore.stateFlow.value.sortAscending)
        stellarExplorerStore.send(action = StellarExplorerAction.ChangeSortDirection)
        assertEquals(actual = false, expected = stellarExplorerStore.stateFlow.value.sortAscending)
    }

    @Test
    fun `send action change visibility`() = runBlocking {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeStellarHostsVisibility(property = StellarHostProperty.NAME))
        assertEquals(
            actual = StellarHostProperty.entries - StellarHostProperty.NAME,
            expected = stellarExplorerStore.stateFlow.value.visibleStellarHostProperties
        )

        stellarExplorerStore.send(action = StellarExplorerAction.ChangePlanetVisibility(property = PlanetProperty.NAME))
        assertEquals(
            actual = PlanetProperty.entries - PlanetProperty.NAME,
            expected = stellarExplorerStore.stateFlow.value.visiblePlanetProperties
        )
    }
}
