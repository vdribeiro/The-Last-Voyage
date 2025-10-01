package com.hybris.tlv.ui.screen.stellarexplorer

import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.storeFactory
import com.hybris.tlv.testCore
import com.hybris.tlv.ui.navigation.NavigationManager
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class StellarExplorerStoreTest {

    private val store: StellarExplorerStore get() = storeFactory.createStellarExplorerStore()

    @BeforeTest
    fun setup() = runBlocking {
        testCore.clearDatabase()
        testCore.navigation.navigate(screen = NavigationManager.Screen.Splash)
        testCore.navigation.navigate(screen = NavigationManager.Screen.MainMenu)
        testCore.navigation.navigate(screen = NavigationManager.Screen.StellarExplorer)
    }

    @Test
    fun `init`() = runBlocking {
        testCore.useCases.space.prepopulateStellarHosts()
        testCore.useCases.space.prepopulatePlanets()
        val stellarExplorerStore = store
        val state = stellarExplorerStore.stateFlow.value
        assertEquals(expected = Content.LIST_HOSTS, actual = state.currentContent)
        assertEquals(expected = emptyList(), actual = state.filteredPlanets)
    }

    @Test
    fun `send action back`() = runBlocking {
        val stellarExplorerStore = store
        testCore.navigation.navigate(screen = NavigationManager.Screen.StellarExplorer)

        stellarExplorerStore.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = stellarHosts.first()))
        assertEquals(expected = Content.DETAIL_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        testCore.navigation.back()
        assertEquals(expected = Content.LIST_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        assertEquals(expected = Content.LIST_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.OpenPlanet(planet = planets.first()))
        assertEquals(expected = Content.DETAIL_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        testCore.navigation.back()
        assertEquals(expected = Content.LIST_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        testCore.navigation.back()
        assertEquals(expected = NavigationManager.Screen.MainMenu, actual = testCore.navigation.stateFlow.value.screen)
    }

    @Test
    fun `send action save index`() = runBlocking {
        val stellarExplorerStore = store
        assertEquals(expected = LazyListIndex(), actual = stellarExplorerStore.stateFlow.value.listIndex)
        stellarExplorerStore.send(action = StellarExplorerAction.SaveIndex(index = LazyListIndex(index = 6, scrollOffset = 9)))
        assertEquals(expected = LazyListIndex(index = 6, scrollOffset = 9), actual = stellarExplorerStore.stateFlow.value.listIndex)
    }

    @Test
    fun `send action search`() = runBlocking {
        testCore.useCases.space.prepopulateStellarHosts()
        testCore.useCases.space.prepopulatePlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.Search(search = stellarHosts.first().id))
        assertEquals(expected = listOf(stellarHosts.first()), actual = stellarExplorerStore.stateFlow.value.filteredStellarHosts)

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        stellarExplorerStore.send(action = StellarExplorerAction.Search(search = planets.first().id))
        assertEquals(expected = listOf(planets.first()), actual = stellarExplorerStore.stateFlow.value.filteredPlanets)
    }

    @Test
    fun `send action sort`() = runBlocking {
        testCore.useCases.space.prepopulateStellarHosts()
        testCore.useCases.space.prepopulatePlanets()
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
    fun `send action change visibility`() = runBlocking {
        testCore.useCases.space.prepopulateStellarHosts()
        testCore.useCases.space.prepopulatePlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeStellarHostsVisibility(property = StellarHostProperty.NAME))
        val visibleStellarHostProperties: Set<StellarHostProperty> = setOf(
            StellarHostProperty.SYSTEM_NAME,
            StellarHostProperty.PLANET_COUNT,
            StellarHostProperty.SPECTRAL_TYPE,
            StellarHostProperty.TEMPERATURE,
            StellarHostProperty.RADIUS,
            StellarHostProperty.MASS,
            StellarHostProperty.METALLICITY,
            StellarHostProperty.LUMINOSITY,
            StellarHostProperty.GRAVITY,
            StellarHostProperty.AGE,
            StellarHostProperty.DENSITY,
            StellarHostProperty.ROTATIONAL_VELOCITY,
            StellarHostProperty.ROTATIONAL_PERIOD,
            StellarHostProperty.DISTANCE,
            StellarHostProperty.RA,
            StellarHostProperty.DEC,
        )
        assertEquals(
            expected = visibleStellarHostProperties,
            actual = stellarExplorerStore.stateFlow.value.visibleStellarHostProperties
        )

        stellarExplorerStore.send(action = StellarExplorerAction.ChangePlanetVisibility(property = PlanetProperty.NAME))
        val visiblePlanetProperties: Set<PlanetProperty> = setOf(
            PlanetProperty.STATUS,
            PlanetProperty.HABITABILITY,
            PlanetProperty.CONFIDENCE,
            PlanetProperty.TYPE,
            PlanetProperty.ORBITAL_PERIOD,
            PlanetProperty.ORBIT_AXIS,
            PlanetProperty.RADIUS,
            PlanetProperty.MASS,
            PlanetProperty.DENSITY,
            PlanetProperty.ECCENTRICITY,
            PlanetProperty.INSOLATION_FLUX,
            PlanetProperty.TEMPERATURE,
            PlanetProperty.OCCULTATION_DEPTH,
            PlanetProperty.INCLINATION,
            PlanetProperty.OBLIQUITY,
        )
        assertEquals(
            expected = visiblePlanetProperties,
            actual = stellarExplorerStore.stateFlow.value.visiblePlanetProperties
        )
    }

    @Test
    fun `send action change searchable`() = runBlocking {
        testCore.useCases.space.prepopulateStellarHosts()
        testCore.useCases.space.prepopulatePlanets()
        val stellarExplorerStore = store

        assertEquals(expected = setOf(StellarHostProperty.NAME), actual = stellarExplorerStore.stateFlow.value.searchableStellarHostProperties)
        stellarExplorerStore.send(action = StellarExplorerAction.ChangeStellarHostsSearchable(property = StellarHostProperty.NAME))
        assertEquals(expected = emptySet(), actual = stellarExplorerStore.stateFlow.value.searchableStellarHostProperties)

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)

        assertEquals(expected = setOf(PlanetProperty.NAME), actual = stellarExplorerStore.stateFlow.value.searchablePlanetProperties)
        stellarExplorerStore.send(action = StellarExplorerAction.ChangePlanetSearchable(property = PlanetProperty.NAME))
        assertEquals(expected = emptySet(), actual = stellarExplorerStore.stateFlow.value.searchablePlanetProperties)

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
    }
}
