package com.hybris.tlv.ui.screen.stellarexplorer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.firstOrNull
import androidx.compose.foundation.lazy.LazyListState
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.navigation.Screen

internal class StellarExplorerStoreTest: TestCase() {

    @Test
    fun init() = runUnitTest {
        dependency.get().useCases.space.syncStellarHosts()
        dependency.get().useCases.space.syncPlanets()
        val store = storeFactory.get().getStellarExplorerStore()
        store.stateFlow.firstOrNull() // Trigger observe
        assertFalse(store.state.loading)
        assertEquals(expected = Content.LIST_HOSTS, actual = store.state.currentContent)
        assertEquals(expected = 0, actual = store.state.listState.firstVisibleItemIndex)
        assertEquals(expected = 0, actual = store.state.listState.firstVisibleItemScrollOffset)
        assertEquals(expected = FakeData.stellarHosts.get().map { it.id }.sorted(), actual = store.state.exoplanets.stellarHosts.map { it.id }.sorted())
        assertNull(actual = store.selectedStellarHost)
        assertNull(actual = store.selectedPlanet)
        assertTrue(actual = store.state.search.isEmpty())
        assertEquals(expected = StellarHostProperty.DISTANCE.name, actual = store.sortStellarHostProperty)
        assertEquals(expected = PlanetProperty.HABITABILITY.name, actual = store.sortPlanetProperty)
        assertTrue(actual = store.state.sortAscending)
        assertEquals(
            expected = listOf(
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
            ).map { it.name },
            actual = store.visibleStellarHostProperties
        )
        assertEquals(
            expected = listOf(
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
            ).map { it.name },
            actual = store.visiblePlanetProperties
        )
        assertEquals(expected = listOf(StellarHostProperty.NAME).map { it.name }, actual = store.searchableStellarHostProperties)
        assertEquals(expected = listOf(PlanetProperty.NAME).map { it.name }, actual = store.searchablePlanetProperties)
    }

    @Test
    fun saveIndex() = runUnitTest {
        val store = storeFactory.get().getStellarExplorerStore()
        assertEquals(expected = 0, actual = store.state.listState.firstVisibleItemIndex)
        assertEquals(expected = 0, actual = store.state.listState.firstVisibleItemScrollOffset)

        val lazyListState = LazyListState(firstVisibleItemIndex = 6, firstVisibleItemScrollOffset = 9)
        store.send(action = StellarExplorerAction.SaveListState(listState = lazyListState))
        assertEquals(expected = lazyListState.firstVisibleItemIndex, actual = store.state.listState.firstVisibleItemIndex)
        assertEquals(expected = lazyListState.firstVisibleItemScrollOffset, actual = store.state.listState.firstVisibleItemScrollOffset)
    }

    @Test
    fun search() = runUnitTest {
        dependency.get().useCases.space.syncStellarHosts()
        dependency.get().useCases.space.syncPlanets()
        val store = storeFactory.get().getStellarExplorerStore()
        store.stateFlow.firstOrNull() // Trigger observe

        store.send(action = StellarExplorerAction.Search(search = FakeData.stellarHosts.get().first().name))
        assertEquals(expected = FakeData.stellarHosts.get().first().id, actual = store.state.exoplanets.stellarHosts.first().id)

        store.send(action = StellarExplorerAction.ChangeView)
        store.send(action = StellarExplorerAction.Search(search = FakeData.planets.get().first().name))
        assertEquals(expected = FakeData.planets.get().first().id, actual = store.state.exoplanets.planets.first().id)
    }

    @Test
    fun sort() = runUnitTest {
        dependency.get().useCases.space.syncStellarHosts()
        dependency.get().useCases.space.syncPlanets()
        val store = storeFactory.get().getStellarExplorerStore()

        store.send(action = StellarExplorerAction.Sort(sort = StellarHostProperty.NAME.name))
        assertEquals(expected = StellarHostProperty.NAME.name, actual = store.sortStellarHostProperty)

        assertTrue(actual = store.state.sortAscending)
        store.send(action = StellarExplorerAction.ChangeSortDirection)
        assertFalse(actual = store.state.sortAscending)
    }

    @Test
    fun changeVisibility() = runUnitTest {
        dependency.get().useCases.space.syncStellarHosts()
        dependency.get().useCases.space.syncPlanets()
        val store = storeFactory.get().getStellarExplorerStore()

        store.send(action = StellarExplorerAction.ChangeVisibility(property = StellarHostProperty.NAME.name))
        assertEquals(
            expected = listOf(
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
            ).map { it.name },
            actual = store.visibleStellarHostProperties
        )
    }

    @Test
    fun changeSearchable() = runUnitTest {
        dependency.get().useCases.space.syncStellarHosts()
        dependency.get().useCases.space.syncPlanets()
        val store = storeFactory.get().getStellarExplorerStore()

        assertEquals(expected = listOf(StellarHostProperty.NAME).map { it.name }, actual = store.searchableStellarHostProperties)
        store.send(action = StellarExplorerAction.ChangeSearchable(property = StellarHostProperty.NAME.name))
        assertEquals(expected = emptyList(), actual = store.searchableStellarHostProperties)

        store.send(action = StellarExplorerAction.ChangeView)

        assertEquals(expected = listOf(PlanetProperty.NAME).map { it.name }, actual = store.searchablePlanetProperties)
        store.send(action = StellarExplorerAction.ChangeSearchable(property = PlanetProperty.NAME.name))
        assertEquals(expected = emptyList(), actual = store.searchablePlanetProperties)
    }

    @Test
    fun navigateBack() = runUnitTest {
        assertNavigation(list = emptyList())
        navigate(screen = Screen.StellarExplorer)
        assertNavigation(list = listOf(Screen.StellarExplorer))
        val store = storeFactory.get().getStellarExplorerStore()

        store.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = FakeData.stellarHosts.get().first()))
        assertEquals(expected = Content.DETAIL_HOSTS, actual = store.state.currentContent)
        store.send(action = StellarExplorerAction.Back)
        assertEquals(expected = Content.LIST_HOSTS, actual = store.state.currentContent)
        store.send(action = StellarExplorerAction.ChangeView)
        assertEquals(expected = Content.LIST_PLANETS, actual = store.state.currentContent)
        store.send(action = StellarExplorerAction.OpenPlanet(planet = FakeData.planets.get().first()))
        assertEquals(expected = Content.DETAIL_PLANETS, actual = store.state.currentContent)
        store.send(action = StellarExplorerAction.Back)
        assertEquals(expected = Content.LIST_PLANETS, actual = store.state.currentContent)
        store.send(action = StellarExplorerAction.Back)
        assertNavigation(list = emptyList())
    }
}
