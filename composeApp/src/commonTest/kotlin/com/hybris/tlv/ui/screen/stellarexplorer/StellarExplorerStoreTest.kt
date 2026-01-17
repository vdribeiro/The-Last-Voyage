package com.hybris.tlv.ui.screen.stellarexplorer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import androidx.compose.foundation.lazy.LazyListState
import com.hybris.tlv.TestCase
import com.hybris.tlv.domain.usecase.space.model.Formula
import com.hybris.tlv.planets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.ui.navigation.Screen

internal class StellarExplorerStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val store = storeFactory.getStellarExplorerStore()
        assertEquals(expected = Formula(id = store.formula.id), actual = store.formula)
        assertTrue(actual = store.stellarHostsFlow.value.isNotEmpty())
        assertFalse(store.state.loading)
        assertEquals(expected = Content.LIST_HOSTS, actual = store.state.currentContent)
        assertEquals(expected = 0, actual = store.state.listState.firstVisibleItemIndex)
        assertEquals(expected = 0, actual = store.state.listState.firstVisibleItemScrollOffset)
        assertEquals(expected = stellarHosts.sortedBy { it.id }, actual = store.state.stellarHosts.sortedBy { it.id })
        assertTrue(actual = store.state.planets.isEmpty())
        assertNull(actual = store.state.selectedStellarHost)
        assertNull(actual = store.state.selectedPlanet)
        assertTrue(actual = store.state.search.isEmpty())
        assertEquals(expected = StellarHostProperty.DISTANCE, actual = store.state.sortStellarHostProperty)
        assertEquals(expected = PlanetProperty.HABITABILITY, actual = store.state.sortPlanetProperty)
        assertTrue(actual = store.state.sortAscending)
        assertEquals(
            expected = setOf(
                StellarHostProperty.NAME,
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
            ),
            actual = store.state.visibleStellarHostProperties
        )
        assertEquals(
            expected = setOf(
                PlanetProperty.NAME,
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
            ),
            actual = store.state.visiblePlanetProperties
        )
        assertEquals(expected = setOf(StellarHostProperty.NAME), actual = store.state.searchableStellarHostProperties)
        assertEquals(expected = setOf(PlanetProperty.NAME), actual = store.state.searchablePlanetProperties)
    }

    @Test
    fun saveIndex() = runUnitTest {
        val store = storeFactory.getStellarExplorerStore()
        assertEquals(expected = 0, actual = store.state.listState.firstVisibleItemIndex)
        assertEquals(expected = 0, actual = store.state.listState.firstVisibleItemScrollOffset)

        val lazyListState = LazyListState(firstVisibleItemIndex = 6, firstVisibleItemScrollOffset = 9)
        store.send(action = StellarExplorerAction.SaveListState(listState = lazyListState))
        assertEquals(expected = lazyListState.firstVisibleItemIndex, actual = store.state.listState.firstVisibleItemIndex)
        assertEquals(expected = lazyListState.firstVisibleItemScrollOffset, actual = store.state.listState.firstVisibleItemScrollOffset)
    }

    @Test
    fun search() = runUnitTest {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val store = storeFactory.getStellarExplorerStore()

        store.send(action = StellarExplorerAction.Search(search = stellarHosts.first().name))
        assertEquals(expected = listOf(stellarHosts.first()), actual = store.state.stellarHosts)

        store.send(action = StellarExplorerAction.ChangeView)
        store.send(action = StellarExplorerAction.Search(search = planets.first().name))
        assertEquals(expected = listOf(planets.first()), actual = store.state.planets)
    }

    @Test
    fun sort() = runUnitTest {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val store = storeFactory.getStellarExplorerStore()

        store.send(action = StellarExplorerAction.SortStellarHosts(sort = StellarHostProperty.NAME))
        assertEquals(expected = StellarHostProperty.NAME, actual = store.state.sortStellarHostProperty)

        store.send(action = StellarExplorerAction.SortPlanets(sort = PlanetProperty.NAME))
        assertEquals(expected = PlanetProperty.NAME, actual = store.state.sortPlanetProperty)

        assertTrue(actual = store.state.sortAscending)
        store.send(action = StellarExplorerAction.ChangeSortDirection)
        assertFalse(actual = store.state.sortAscending)
    }

    @Test
    fun changeVisibility() = runUnitTest {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val store = storeFactory.getStellarExplorerStore()

        store.send(action = StellarExplorerAction.ChangeStellarHostsVisibility(property = StellarHostProperty.NAME))
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
        assertEquals(expected = visibleStellarHostProperties, actual = store.state.visibleStellarHostProperties)

        store.send(action = StellarExplorerAction.ChangePlanetVisibility(property = PlanetProperty.NAME))
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
        assertEquals(expected = visiblePlanetProperties, actual = store.state.visiblePlanetProperties)
    }

    @Test
    fun changeSearchable() = runUnitTest {
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val store = storeFactory.getStellarExplorerStore()

        assertEquals(expected = setOf(StellarHostProperty.NAME), actual = store.state.searchableStellarHostProperties)
        store.send(action = StellarExplorerAction.ChangeStellarHostsSearchable(property = StellarHostProperty.NAME))
        assertEquals(expected = emptySet(), actual = store.state.searchableStellarHostProperties)

        store.send(action = StellarExplorerAction.ChangeView)

        assertEquals(expected = setOf(PlanetProperty.NAME), actual = store.state.searchablePlanetProperties)
        store.send(action = StellarExplorerAction.ChangePlanetSearchable(property = PlanetProperty.NAME))
        assertEquals(expected = emptySet(), actual = store.state.searchablePlanetProperties)

        store.send(action = StellarExplorerAction.ChangeView)
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.StellarExplorer)
        assertNavigation(list = listOf(Screen.StellarExplorer))
        val store = storeFactory.getStellarExplorerStore()

        store.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = stellarHosts.first()))
        assertEquals(expected = Content.DETAIL_HOSTS, actual = store.state.currentContent)
        store.back()
        assertEquals(expected = Content.LIST_HOSTS, actual = store.state.currentContent)
        store.send(action = StellarExplorerAction.ChangeView)
        assertEquals(expected = Content.LIST_PLANETS, actual = store.state.currentContent)
        store.send(action = StellarExplorerAction.OpenPlanet(planet = planets.first()))
        assertEquals(expected = Content.DETAIL_PLANETS, actual = store.state.currentContent)
        store.back()
        assertEquals(expected = Content.LIST_PLANETS, actual = store.state.currentContent)
        store.back()
        assertNavigation(list = emptyList())
    }
}
