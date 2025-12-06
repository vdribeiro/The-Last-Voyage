package com.hybris.tlv.ui.screen.stellarexplorer

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import androidx.compose.foundation.lazy.LazyListState
import com.hybris.tlv.getStellarExplorerStore
import com.hybris.tlv.planets
import com.hybris.tlv.reset
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.useCases

internal class StellarExplorerStoreTest {

    private val store: StellarExplorerStore get() = getStellarExplorerStore()

    @BeforeTest
    fun setup() = runBlocking {
        reset()
//        getNavigation().navigate(navigationState = NavigationState(screen = SplashScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = MainMenuScreen))
//        getNavigation().navigate(navigationState = NavigationState(screen = StellarExplorerScreen))
    }

    @Test
    fun `init`() = runBlocking {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val stellarExplorerStore = store
        val state = stellarExplorerStore.stateFlow.value
        assertEquals(expected = Content.LIST_HOSTS, actual = state.currentContent)
        assertEquals(expected = emptyList(), actual = state.planets)
    }

    @Test
    fun `send action back`() = runBlocking {
        val stellarExplorerStore = store
//        getNavigation().navigate(navigationState = NavigationState(screen = StellarExplorerScreen))

        stellarExplorerStore.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = stellarHosts.first()))
        assertEquals(expected = Content.DETAIL_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)
//        getNavigation().back()
        assertEquals(expected = Content.LIST_HOSTS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        assertEquals(expected = Content.LIST_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)
        stellarExplorerStore.send(action = StellarExplorerAction.OpenPlanet(planet = planets.first()))
        assertEquals(expected = Content.DETAIL_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)
//        getNavigation().back()
        assertEquals(expected = Content.LIST_PLANETS, actual = stellarExplorerStore.stateFlow.value.currentContent)
//        getNavigation().back()
//        assertEquals(expected = MainMenuScreen, actual = getNavigation().stateFlow.value.screen)
    }

    @Test
    fun `send action save index`() = runBlocking {
        val stellarExplorerStore = store
        assertEquals(expected = LazyListState(), actual = stellarExplorerStore.stateFlow.value.listState)
        stellarExplorerStore.send(action = StellarExplorerAction.SaveListState(listState = LazyListState(firstVisibleItemIndex = 6, firstVisibleItemScrollOffset = 9)))
        assertEquals(expected = LazyListState(firstVisibleItemIndex = 6, firstVisibleItemScrollOffset = 9), actual = stellarExplorerStore.stateFlow.value.listState)
    }

    @Test
    fun `send action search`() = runBlocking {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val stellarExplorerStore = store

        stellarExplorerStore.send(action = StellarExplorerAction.Search(search = stellarHosts.first().name))
        assertEquals(expected = listOf(stellarHosts.first()), actual = stellarExplorerStore.stateFlow.value.stellarHosts)

        stellarExplorerStore.send(action = StellarExplorerAction.ChangeView)
        stellarExplorerStore.send(action = StellarExplorerAction.Search(search = planets.first().name))
        assertEquals(expected = listOf(planets.first()), actual = stellarExplorerStore.stateFlow.value.planets)
    }

    @Test
    fun `send action sort`() = runBlocking {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
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
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
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
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
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
