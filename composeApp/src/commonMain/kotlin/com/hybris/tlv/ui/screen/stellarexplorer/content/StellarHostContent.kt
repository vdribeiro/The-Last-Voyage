package com.hybris.tlv.ui.screen.stellarexplorer.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.stellarexplorer.Content
import com.hybris.tlv.ui.screen.stellarexplorer.LazyListIndex
import com.hybris.tlv.ui.screen.stellarexplorer.PlanetProperty
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerAction
import com.hybris.tlv.ui.screen.stellarexplorer.StellarExplorerState
import com.hybris.tlv.ui.screen.stellarexplorer.StellarHostProperty
import com.hybris.tlv.ui.screen.stellarexplorer.ifContains
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.PlanetCard
import com.hybris.tlv.ui.theme.component.StellarHostCard
import com.hybris.tlv.usecase.space.formula.spectralTypeToDrawable
import com.hybris.tlv.usecase.space.formula.toDrawable

@Composable
internal fun StellarHostContent(store: Store<StellarExplorerAction, StellarExplorerState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent
    val planet = storeState.selectedPlanet
    val visibleStellarHostProperties = storeState.visibleStellarHostProperties
    val visiblePlanetProperties = storeState.visiblePlanetProperties
    val listState = storeState.listIndex.getState()
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
                    name = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.NAME,
                        value = planet.name
                    ),
                    status = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.STATUS,
                        value = planet.status.displayName
                    ),
                    habitability = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.HABITABILITY,
                        value = planet.score?.habitabilityScore
                    ),
                    confidence = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.CONFIDENCE,
                        value = planet.score?.confidenceScore
                    ),
                    orbitalPeriod = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ORBITAL_PERIOD,
                        value = planet.orbitalPeriod
                    ),
                    orbitAxis = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ORBIT_AXIS,
                        value = planet.orbitAxis
                    ),
                    radius = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.RADIUS,
                        value = planet.radius
                    ),
                    mass = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.MASS,
                        value = planet.mass
                    ),
                    density = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.DENSITY,
                        value = planet.density
                    ),
                    eccentricity = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ECCENTRICITY,
                        value = planet.eccentricity
                    ),
                    insolationFlux = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.INSOLATION_FLUX,
                        value = planet.insolationFlux
                    ),
                    equilibriumTemperature = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TEMPERATURE,
                        value = planet.equilibriumTemperature
                    ),
                    occultationDepth = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.OCCULTATION_DEPTH,
                        value = planet.occultationDepth
                    ),
                    inclination = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.INCLINATION,
                        value = planet.inclination
                    ),
                    obliquity = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.OBLIQUITY,
                        value = planet.obliquity
                    ),
                    type = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TYPE,
                        value = planet.score?.planetType?.displayName
                    ),
                    typeDrawable = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TYPE,
                        value = planet.score?.planetType.toDrawable()
                    ),
                    rocheScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ROCHE_SCORE,
                        value = planet.score?.rocheScore
                    ),
                    habitableZoneKopparapuScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.HABITABLE_ZONE_KOPPARAPU_SCORE,
                        value = planet.score?.habitableZoneKopparapuScore
                    ),
                    habitableZoneKastingScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.HABITABLE_ZONE_KASTING_SCORE,
                        value = planet.score?.habitableZoneKastingScore
                    ),
                    radiusScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.RADIUS_SCORE,
                        value = planet.score?.planetRadiusScore
                    ),
                    massScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.MASS_SCORE,
                        value = planet.score?.planetMassScore
                    ),
                    telluricityScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TELLURICITY_SCORE,
                        value = planet.score?.planetTelluricityScore
                    ),
                    eccentricityScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ECCENTRICITY_SCORE,
                        value = planet.score?.planetEccentricityScore
                    ),
                    temperatureScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TEMPERATURE_SCORE,
                        value = planet.score?.planetTemperatureScore
                    ),
                    obliquityScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.OBLIQUITY_SCORE,
                        value = planet.score?.planetObliquityScore
                    ),
                    esiScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.ESI_SCORE,
                        value = planet.score?.planetEsiScore
                    ),
                    protectionScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.PROTECTION_SCORE,
                        value = planet.score?.planetProtectionScore
                    ),
                    tidalLockingScore = visiblePlanetProperties.ifContains(
                        element = PlanetProperty.TIDAL_LOCKING_SCORE,
                        value = planet.score?.planetTidalLockingScore
                    )
                )
            }
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = storeState.filteredStellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                modifier = Modifier.clickable {
                    store.send(
                        action = StellarExplorerAction.SaveIndex(
                            index = LazyListIndex(
                                index = listState.firstVisibleItemIndex,
                                scrollOffset = listState.firstVisibleItemScrollOffset
                            )
                        )
                    )
                    store.send(action = StellarExplorerAction.OpenStellarHost(stellarHost = stellarHost))
                },
                name = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.NAME,
                    value = stellarHost.name
                ),
                systemName = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.SYSTEM_NAME,
                    value = stellarHost.systemName
                ),
                planetCount = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.PLANET_COUNT,
                    value = stellarHost.planets.size
                ),
                spectralType = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.SPECTRAL_TYPE,
                    value = stellarHost.spectralType
                ),
                spectralTypeDrawable = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.SPECTRAL_TYPE,
                    value = stellarHost.spectralType.spectralTypeToDrawable()
                ),
                effectiveTemperature = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.TEMPERATURE,
                    value = stellarHost.effectiveTemperature
                ),
                radius = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.RADIUS,
                    value = stellarHost.radius
                ),
                mass = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.MASS,
                    value = stellarHost.mass
                ),
                metallicity = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.METALLICITY,
                    value = stellarHost.metallicity
                ),
                luminosity = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.LUMINOSITY,
                    value = stellarHost.luminosity
                ),
                gravity = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.GRAVITY,
                    value = stellarHost.gravity
                ),
                age = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.AGE,
                    value = stellarHost.age
                ),
                density = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.DENSITY,
                    value = stellarHost.density
                ),
                rotationalVelocity = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.ROTATIONAL_VELOCITY,
                    value = stellarHost.rotationalVelocity
                ),
                rotationalPeriod = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.ROTATIONAL_PERIOD,
                    value = stellarHost.rotationalPeriod
                ),
                distance = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.DISTANCE,
                    value = stellarHost.distance
                ),
                ra = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.RA,
                    value = stellarHost.ra
                ),
                dec = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.DEC,
                    value = stellarHost.dec
                ),
                spectralTypeScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.SPECTRAL_TYPE_SCORE,
                    value = stellarHost.score?.stellarSpectralTypeScore
                ),
                massScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.MASS_SCORE,
                    value = stellarHost.score?.stellarMassScore
                ),
                ageScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.AGE_SCORE,
                    value = stellarHost.score?.stellarAgeScore
                ),
                activityScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.ACTIVITY_SCORE,
                    value = stellarHost.score?.stellarActivityScore
                ),
                rotationalPeriodScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.ROTATIONAL_PERIOD_SCORE,
                    value = stellarHost.score?.stellarRotationalPeriodScore
                ),
                gravityScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.GRAVITY_SCORE,
                    value = stellarHost.score?.stellarGravityScore
                ),
                metallicityScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.METALLICITY_SCORE,
                    value = stellarHost.score?.stellarMetallicityScore
                ),
                effectiveTemperatureScore = visibleStellarHostProperties.ifContains(
                    element = StellarHostProperty.EFFECTIVE_TEMPERATURE_SCORE,
                    value = stellarHost.score?.stellarEffectiveTemperatureScore
                )
            )
        }
    }
}
