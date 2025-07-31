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
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerAction
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.store.Store

@Composable
internal fun StellarHostContent(store: Store<StellarExplorerAction, StellarExplorerState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent
    val planet = storeState.selectedPlanet

    val listState = if (currentContent == Content.LIST_HOSTS) storeState.listIndex.getState() else rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        state = listState
    ) {
        if (currentContent == Content.DETAIL_PLANETS && planet != null) {
            item(key = planet.id) {
                PlanetCard(
                    name = planet.name,
                    status = planet.status,
                    orbitalPeriod = planet.orbitalPeriod,
                    orbitAxis = planet.orbitAxis,
                    radius = planet.radius,
                    mass = planet.mass,
                    density = planet.density,
                    eccentricity = planet.eccentricity,
                    insolationFlux = planet.insolationFlux,
                    equilibriumTemperature = planet.equilibriumTemperature,
                    occultationDepth = planet.occultationDepth,
                    inclination = planet.inclination,
                    obliquity = planet.obliquity,
                    habitability = planet.habitability?.habitabilityScore,
                )
            }
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = storeState.filteredStellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                name = stellarHost.name,
                systemName = stellarHost.systemName,
                planetCount = stellarHost.planets.size,
                spectralType = stellarHost.spectralType,
                effectiveTemperature = stellarHost.effectiveTemperature,
                radius = stellarHost.radius,
                mass = stellarHost.mass,
                metallicity = stellarHost.metallicity,
                luminosity = stellarHost.luminosity,
                gravity = stellarHost.gravity,
                age = stellarHost.age,
                density = stellarHost.density,
                rotationalVelocity = stellarHost.rotationalVelocity,
                rotationalPeriod = stellarHost.rotationalPeriod,
                distance = stellarHost.distance,
                ra = stellarHost.ra,
                dec = stellarHost.dec,
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
