package com.hybris.tlv.ui.theme.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.divider.Divider
import com.hybris.tlv.ui.theme.component.image.ImageResource

@Composable
internal inline fun <T> StellarHostList(
    modifier: Modifier = Modifier,
    listState: LazyListState = LazyListState(),
    showPlanet: Boolean = false,
    planetId: String? = null,
    planetName: String? = null,
    planetDescription: String? = null,
    planetStatus: String? = null,
    planetOrbitalPeriod: Double? = null,
    planetOrbitAxis: Double? = null,
    planetRadius: Double? = null,
    planetMass: Double? = null,
    planetDensity: Double? = null,
    planetEccentricity: Double? = null,
    planetInsolationFlux: Double? = null,
    planetEquilibriumTemperature: Double? = null,
    planetOccultationDepth: Double? = null,
    planetInclination: Double? = null,
    planetObliquity: Double? = null,
    planetHabitability: Double? = null,
    planetConfidence: Double? = null,
    planetType: String? = null,
    planetImage: ImageResource? = null,
    planetRocheScore: Double? = null,
    planetHabitableZoneKopparapuScore: Double? = null,
    planetHabitableZoneKastingScore: Double? = null,
    planetRadiusScore: Double? = null,
    planetMassScore: Double? = null,
    planetTelluricityScore: Double? = null,
    planetEccentricityScore: Double? = null,
    planetTemperatureScore: Double? = null,
    planetObliquityScore: Double? = null,
    planetEsiScore: Double? = null,
    planetProtectionScore: Double? = null,
    planetTidalLockingScore: Double? = null,
    stellarHosts: List<T> = emptyList(),
    noinline stellarHostId: (T) -> String = { generateUuid() },
    crossinline stellarHostName: (T) -> String? = { null },
    crossinline stellarHostDescription: (T) -> String? = { null },
    crossinline stellarHostSystemName: (T) -> String? = { null },
    crossinline stellarHostPlanetCount: (T) -> Int? = { null },
    crossinline stellarHostSpectralType: (T) -> String? = { null },
    crossinline stellarHostSpectralImage: (T) -> ImageResource? = { null },
    crossinline stellarHostEffectiveTemperature: (T) -> Double? = { null },
    crossinline stellarHostRadius: (T) -> Double? = { null },
    crossinline stellarHostMass: (T) -> Double? = { null },
    crossinline stellarHostMetallicity: (T) -> Double? = { null },
    crossinline stellarHostLuminosity: (T) -> Double? = { null },
    crossinline stellarHostGravity: (T) -> Double? = { null },
    crossinline stellarHostAge: (T) -> Double? = { null },
    crossinline stellarHostDensity: (T) -> Double? = { null },
    crossinline stellarHostRotationalVelocity: (T) -> Double? = { null },
    crossinline stellarHostRotationalPeriod: (T) -> Double? = { null },
    crossinline stellarHostDistance: (T) -> Double? = { null },
    crossinline stellarHostRa: (T) -> Double? = { null },
    crossinline stellarHostDec: (T) -> Double? = { null },
    crossinline stellarHostSpectralTypeScore: (T) -> Double? = { null },
    crossinline stellarHostMassScore: (T) -> Double? = { null },
    crossinline stellarHostAgeScore: (T) -> Double? = { null },
    crossinline stellarHostActivityScore: (T) -> Double? = { null },
    crossinline stellarHostRotationalPeriodScore: (T) -> Double? = { null },
    crossinline stellarHostGravityScore: (T) -> Double? = { null },
    crossinline stellarHostMetallicityScore: (T) -> Double? = { null },
    crossinline stellarHostEffectiveTemperatureScore: (T) -> Double? = { null },
    crossinline onStellarHostClick: (T) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        state = listState
    ) {
        if (showPlanet) {
            item(key = planetId) {
                PlanetCard(
                    name = planetName,
                    description = planetDescription,
                    status = planetStatus,
                    orbitalPeriod = planetOrbitalPeriod,
                    orbitAxis = planetOrbitAxis,
                    radius = planetRadius,
                    mass = planetMass,
                    density = planetDensity,
                    eccentricity = planetEccentricity,
                    insolationFlux = planetInsolationFlux,
                    equilibriumTemperature = planetEquilibriumTemperature,
                    occultationDepth = planetOccultationDepth,
                    inclination = planetInclination,
                    obliquity = planetObliquity,
                    habitability = planetHabitability,
                    confidence = planetConfidence,
                    type = planetType,
                    image = planetImage,
                    rocheScore = planetRocheScore,
                    habitableZoneKopparapuScore = planetHabitableZoneKopparapuScore,
                    habitableZoneKastingScore = planetHabitableZoneKastingScore,
                    radiusScore = planetRadiusScore,
                    massScore = planetMassScore,
                    telluricityScore = planetTelluricityScore,
                    eccentricityScore = planetEccentricityScore,
                    temperatureScore = planetTemperatureScore,
                    obliquityScore = planetObliquityScore,
                    esiScore = planetEsiScore,
                    protectionScore = planetProtectionScore,
                    tidalLockingScore = planetTidalLockingScore
                )
            }
            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = stellarHosts, key = stellarHostId) { stellarHost ->
            StellarHostCard(
                modifier = Modifier
                    .clickable { onStellarHostClick(stellarHost) },
                name = stellarHostName(stellarHost),
                description = stellarHostDescription(stellarHost),
                systemName = stellarHostSystemName(stellarHost),
                planetCount = stellarHostPlanetCount(stellarHost),
                spectralType = stellarHostSpectralType(stellarHost),
                spectralImage = stellarHostSpectralImage(stellarHost),
                effectiveTemperature = stellarHostEffectiveTemperature(stellarHost),
                radius = stellarHostRadius(stellarHost),
                mass = stellarHostMass(stellarHost),
                metallicity = stellarHostMetallicity(stellarHost),
                luminosity = stellarHostLuminosity(stellarHost),
                gravity = stellarHostGravity(stellarHost),
                age = stellarHostAge(stellarHost),
                density = stellarHostDensity(stellarHost),
                rotationalVelocity = stellarHostRotationalVelocity(stellarHost),
                rotationalPeriod = stellarHostRotationalPeriod(stellarHost),
                distance = stellarHostDistance(stellarHost),
                ra = stellarHostRa(stellarHost),
                dec = stellarHostDec(stellarHost),
                spectralTypeScore = stellarHostSpectralTypeScore(stellarHost),
                massScore = stellarHostMassScore(stellarHost),
                ageScore = stellarHostAgeScore(stellarHost),
                activityScore = stellarHostActivityScore(stellarHost),
                rotationalPeriodScore = stellarHostRotationalPeriodScore(stellarHost),
                gravityScore = stellarHostGravityScore(stellarHost),
                metallicityScore = stellarHostMetallicityScore(stellarHost),
                effectiveTemperatureScore = stellarHostEffectiveTemperatureScore(stellarHost)
            )
        }
    }
}
