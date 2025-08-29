package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.ui.component.LazyListIndex
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.stellarexplorer.model.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.model.StellarHostProperty
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

internal class StellarExplorerStoreTest {

    private val store
        get() = StellarExplorerStore(
            dispatcher = mock.dispatcher,
            navigation = mock.navigation,
            initialState = StellarExplorerState(),
            spaceUseCases = mock.useCases.space
        )

    @BeforeTest
    fun setup() = runTest {
        mock.sqlDriver.clearDatabase()
        mock.navigation.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)
    }

    @Test
    fun `init`() = runTest {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        val stellarExplorerStore = store
        assertEquals(expected = Content.LIST_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        val stellarHosts = stellarExplorerStore.stateFlow.value.stellarHosts
        val planets = stellarExplorerStore.stateFlow.value.planets
        assertEquals(expected = stellarHosts, actual = stellarExplorerStore.stateFlow.value.filteredStellarHosts)
        assertEquals(expected = planets, actual = stellarExplorerStore.stateFlow.value.filteredPlanets)
    }

    @Test
    fun `send action back`() = runTest {
        val stellarExplorerStore = store
        mock.navigation.navigate(screen = NavigationManager.Screen.STELLAR_EXPLORER)

        assertEquals(expected = Content.LIST_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        assertEquals(expected = Content.LIST_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)

        stellarExplorerStore.send(action = StellarExplorerAction.OpenPlanet(planet = planets.first()))
        assertEquals(expected = Content.DETAIL_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = Content.LIST_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        stellarExplorerStore.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = stellarHosts.first()))
        assertEquals(expected = Content.DETAIL_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = Content.LIST_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)

        assertEquals(expected = NavigationManager.Screen.STELLAR_EXPLORER, actual = mock.navigation.stateFlow.value.screen)
        assertEquals(expected = Content.LIST_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        mock.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MAIN_MENU, actual = mock.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action save index`() = runTest {
        val stellarExplorerStore = store
        assertEquals(expected = LazyListIndex(), actual = stellarExplorerStore.stateFlow.value.listIndex)
        stellarExplorerStore.send(action = StellarExplorerAction.SaveIndex(index = LazyListIndex(index = 6, scrollOffset = 9)))
        assertEquals(expected = LazyListIndex(index = 6, scrollOffset = 9), actual = stellarExplorerStore.stateFlow.value.listIndex)
    }

    @Test
    fun `send action search`() = runTest {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.Search(search = stellarHosts.first().id))
        assertEquals(expected = listOf(stellarHosts.first()), actual = stellarExplorerStore.stateFlow.value.filteredStellarHosts)

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        stellarExplorerStore.send(action = StellarExplorerAction.Search(search = planets.first().id))
        assertEquals(expected = listOf(planets.first()), actual = stellarExplorerStore.stateFlow.value.filteredPlanets)
    }

    @Test
    fun `send action sort`() = runTest {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.SortStellarHosts(sort = StellarHostProperty.NAME))
        assertEquals(expected = StellarHostProperty.NAME, actual = stellarExplorerStore.stateFlow.value.sortStellarHostProperty)

        stellarExplorerStore.send(action = StellarExplorerAction.SortPlanets(sort = PlanetProperty.NAME))
        assertEquals(expected = PlanetProperty.NAME, actual = stellarExplorerStore.stateFlow.value.sortPlanetProperty)

        assertEquals(expected = true, actual = stellarExplorerStore.stateFlow.value.sortAscending)
        stellarExplorerStore.send(action = StellarExplorerAction.ChangeSortDirection)
        assertEquals(expected = false, actual = stellarExplorerStore.stateFlow.value.sortAscending)
    }

    @Test
    fun `send action change visibility`() = runTest {
        mock.internalSpace.syncStellarHosts()
        mock.internalSpace.syncPlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeStellarHostsVisibility(property = StellarHostProperty.NAME))
        assertEquals(
            expected = StellarHostProperty.entries - StellarHostProperty.NAME,
            actual = stellarExplorerStore.stateFlow.value.visibleStellarHostProperties
        )

        stellarExplorerStore.send(action = StellarExplorerAction.ChangePlanetVisibility(property = PlanetProperty.NAME))
        assertEquals(
            expected = PlanetProperty.entries - PlanetProperty.NAME,
            actual = stellarExplorerStore.stateFlow.value.visiblePlanetProperties
        )
    }
}
