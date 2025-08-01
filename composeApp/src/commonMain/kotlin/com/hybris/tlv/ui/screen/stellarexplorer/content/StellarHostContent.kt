package com.hybris.tlv.ui.screen.stellarexplorer.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.LazyListIndex
import com.hybris.tlv.ui.component.PlanetCard
import com.hybris.tlv.ui.component.StellarHostCard
import com.hybris.tlv.ui.screen.stellarexplorer.Content
import com.hybris.tlv.ui.screen.stellarexplorer.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerAction
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarHostProperty
import com.hybris.tlv.ui.store.Store

@Composable
internal fun StellarHostContent(store: Store<StellarExplorerAction, StellarExplorerState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent
    val planet = storeState.selectedPlanet
    val visibleStellarHostProperties = storeState.visibleStellarHostProperties
    val visiblePlanetProperties = storeState.visiblePlanetProperties

    val listState = if (currentContent == Content.LIST_HOSTS) storeState.listIndex.getState() else rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        state = listState
    ) {
        if (currentContent == Content.DETAIL_PLANETS && planet != null) {
            item(key = planet.id) {
                PlanetCard(
                    name = if (visiblePlanetProperties.contains(element = PlanetProperty.NAME)) planet.name else null,
                    status = if (visiblePlanetProperties.contains(element = PlanetProperty.STATUS)) planet.status else null,
                    orbitalPeriod = if (visiblePlanetProperties.contains(element = PlanetProperty.ORBITAL_PERIOD)) planet.orbitalPeriod else null,
                    orbitAxis = if (visiblePlanetProperties.contains(element = PlanetProperty.ORBIT_AXIS)) planet.orbitAxis else null,
                    radius = if (visiblePlanetProperties.contains(element = PlanetProperty.RADIUS)) planet.radius else null,
                    mass = if (visiblePlanetProperties.contains(element = PlanetProperty.MASS)) planet.mass else null,
                    density = if (visiblePlanetProperties.contains(element = PlanetProperty.DENSITY)) planet.density else null,
                    eccentricity = if (visiblePlanetProperties.contains(element = PlanetProperty.ECCENTRICITY)) planet.eccentricity else null,
                    insolationFlux = if (visiblePlanetProperties.contains(element = PlanetProperty.INSOLATION_FLUX)) planet.insolationFlux else null,
                    equilibriumTemperature = if (visiblePlanetProperties.contains(element = PlanetProperty.TEMPERATURE)) planet.equilibriumTemperature else null,
                    occultationDepth = if (visiblePlanetProperties.contains(element = PlanetProperty.OCCULTATION_DEPTH)) planet.occultationDepth else null,
                    inclination = if (visiblePlanetProperties.contains(element = PlanetProperty.INCLINATION)) planet.inclination else null,
                    obliquity = if (visiblePlanetProperties.contains(element = PlanetProperty.OBLIQUITY)) planet.obliquity else null,
                    habitability = if (visiblePlanetProperties.contains(element = PlanetProperty.HABITABILITY)) planet.habitability?.habitabilityScore else null,
                    type = if (visiblePlanetProperties.contains(element = PlanetProperty.TYPE)) planet.habitability?.planetType else null
                )
            }
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = storeState.filteredStellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                name = if (visibleStellarHostProperties.contains(element = StellarHostProperty.NAME)) stellarHost.name else null,
                systemName = if (visibleStellarHostProperties.contains(element = StellarHostProperty.SYSTEM_NAME)) stellarHost.systemName else null,
                planetCount = if (visibleStellarHostProperties.contains(element = StellarHostProperty.PLANET_COUNT)) stellarHost.planets.size else null,
                spectralType = if (visibleStellarHostProperties.contains(element = StellarHostProperty.SPECTRAL_TYPE)) stellarHost.spectralType else null,
                effectiveTemperature = if (visibleStellarHostProperties.contains(element = StellarHostProperty.TEMPERATURE)) stellarHost.effectiveTemperature else null,
                radius = if (visibleStellarHostProperties.contains(element = StellarHostProperty.RADIUS)) stellarHost.radius else null,
                mass = if (visibleStellarHostProperties.contains(element = StellarHostProperty.MASS)) stellarHost.mass else null,
                metallicity = if (visibleStellarHostProperties.contains(element = StellarHostProperty.METALLICITY)) stellarHost.metallicity else null,
                luminosity = if (visibleStellarHostProperties.contains(element = StellarHostProperty.LUMINOSITY)) stellarHost.luminosity else null,
                gravity = if (visibleStellarHostProperties.contains(element = StellarHostProperty.GRAVITY)) stellarHost.gravity else null,
                age = if (visibleStellarHostProperties.contains(element = StellarHostProperty.AGE)) stellarHost.age else null,
                density = if (visibleStellarHostProperties.contains(element = StellarHostProperty.DENSITY)) stellarHost.density else null,
                rotationalVelocity = if (visibleStellarHostProperties.contains(element = StellarHostProperty.ROTATIONAL_VELOCITY)) stellarHost.rotationalVelocity else null,
                rotationalPeriod = if (visibleStellarHostProperties.contains(element = StellarHostProperty.ROTATIONAL_PERIOD)) stellarHost.rotationalPeriod else null,
                distance = if (visibleStellarHostProperties.contains(element = StellarHostProperty.DISTANCE)) stellarHost.distance else null,
                ra = if (visibleStellarHostProperties.contains(element = StellarHostProperty.RA)) stellarHost.ra else null,
                dec = if (visibleStellarHostProperties.contains(element = StellarHostProperty.DEC)) stellarHost.dec else null,
            ) {
                store.send(
                    action = StellarExplorerAction.SaveIndex(
                        index = LazyListIndex(
                            index = listState.firstVisibleItemIndex,
                            scrollOffset = listState.firstVisibleItemScrollOffset
                        )
                    )
                )
                store.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = stellarHost))
            }
        }
    }
}
