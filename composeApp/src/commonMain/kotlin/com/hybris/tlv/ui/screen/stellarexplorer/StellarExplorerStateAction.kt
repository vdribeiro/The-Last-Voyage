package com.hybris.tlv.ui.screen.stellarexplorer

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import com.hybris.tlv.ui.screen.stellarexplorer.model.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.model.StellarHostProperty
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal data class StellarExplorerState(
    val loading: Boolean = true,
    val currentContent: Content = Content.LIST_HOSTS,
    val stellarHosts: List<StellarHost> = emptyList(),
    val planets: List<Planet> = emptyList(),
    val listIndex: LazyListIndex = LazyListIndex(),
    val filteredStellarHosts: List<StellarHost> = emptyList(),
    val filteredPlanets: List<Planet> = emptyList(),
    val selectedStellarHost: StellarHost? = null,
    val selectedPlanet: Planet? = null,
    val search: String = "",
    val sortStellarHostProperty: StellarHostProperty = StellarHostProperty.DISTANCE,
    val sortPlanetProperty: PlanetProperty = PlanetProperty.NAME,
    val sortAscending: Boolean = true,
    val visibleStellarHostProperties: Set<StellarHostProperty> = setOf(
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
    val visiblePlanetProperties: Set<PlanetProperty> = setOf(
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
    val searchableStellarHostProperties: Set<StellarHostProperty> = setOf(StellarHostProperty.NAME),
    val searchablePlanetProperties: Set<PlanetProperty> = setOf(PlanetProperty.NAME)
)

internal enum class Content {
    LIST_HOSTS,
    DETAIL_HOSTS,
    LIST_PLANETS,
    DETAIL_PLANETS,
}

internal data class LazyListIndex(
    val index: Int = 0,
    val scrollOffset: Int = 0
) {
    @Composable
    fun getState() = rememberLazyListState(
        initialFirstVisibleItemIndex = index,
        initialFirstVisibleItemScrollOffset = scrollOffset
    )
}

internal sealed interface StellarExplorerAction {
    data class SaveIndex(val index: LazyListIndex): StellarExplorerAction
    data object ChangeView: StellarExplorerAction
    data class Search(val search: String): StellarExplorerAction
    data class OpenStellarHost(val stellarHost: StellarHost): StellarExplorerAction
    data class OpenPlanet(val planet: Planet): StellarExplorerAction
    data class SortStellarHosts(val sort: StellarHostProperty): StellarExplorerAction
    data class SortPlanets(val sort: PlanetProperty): StellarExplorerAction
    data object ChangeSortDirection: StellarExplorerAction
    data class ChangeStellarHostsVisibility(val property: StellarHostProperty): StellarExplorerAction
    data class ChangePlanetVisibility(val property: PlanetProperty): StellarExplorerAction
    data class ChangeStellarHostsSearchable(val property: StellarHostProperty): StellarExplorerAction
    data class ChangePlanetSearchable(val property: PlanetProperty): StellarExplorerAction
}
