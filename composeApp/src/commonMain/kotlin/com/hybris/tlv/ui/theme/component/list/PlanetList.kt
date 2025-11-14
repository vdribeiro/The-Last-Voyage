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
internal inline fun <T> PlanetList(
    modifier: Modifier = Modifier,
    listState: LazyListState = LazyListState(),
    showStellarHost: Boolean = false,
    stellarHostId: String? = null,
    stellarHostName: String? = null,
    stellarHostDescription: String? = null,
    stellarHostSystemName: String? = null,
    stellarHostPlanetCount: Int? = null,
    stellarHostSpectralType: String? = null,
    stellarHostSpectralImage: ImageResource? = null,
    stellarHostEffectiveTemperature: Double? = null,
    stellarHostRadius: Double? = null,
    stellarHostMass: Double? = null,
    stellarHostMetallicity: Double? = null,
    stellarHostLuminosity: Double? = null,
    stellarHostGravity: Double? = null,
    stellarHostAge: Double? = null,
    stellarHostDensity: Double? = null,
    stellarHostRotationalVelocity: Double? = null,
    stellarHostRotationalPeriod: Double? = null,
    stellarHostDistance: Double? = null,
    stellarHostRa: Double? = null,
    stellarHostDec: Double? = null,
    stellarHostSpectralTypeScore: Double? = null,
    stellarHostMassScore: Double? = null,
    stellarHostAgeScore: Double? = null,
    stellarHostActivityScore: Double? = null,
    stellarHostRotationalPeriodScore: Double? = null,
    stellarHostGravityScore: Double? = null,
    stellarHostMetallicityScore: Double? = null,
    stellarHostEffectiveTemperatureScore: Double? = null,
    planets: List<T> = emptyList(),
    noinline planetId: (T) -> String = { generateUuid() },
    crossinline planetName: (T) -> String? = { null },
    crossinline planetDescription: (T) -> String? = { null },
    crossinline planetStatus: (T) -> String? = { null },
    crossinline planetOrbitalPeriod: (T) -> Double? = { null },
    crossinline planetOrbitAxis: (T) -> Double? = { null },
    crossinline planetRadius: (T) -> Double? = { null },
    crossinline planetMass: (T) -> Double? = { null },
    crossinline planetDensity: (T) -> Double? = { null },
    crossinline planetEccentricity: (T) -> Double? = { null },
    crossinline planetInsolationFlux: (T) -> Double? = { null },
    crossinline planetEquilibriumTemperature: (T) -> Double? = { null },
    crossinline planetOccultationDepth: (T) -> Double? = { null },
    crossinline planetInclination: (T) -> Double? = { null },
    crossinline planetObliquity: (T) -> Double? = { null },
    crossinline planetHabitability: (T) -> Double? = { null },
    crossinline planetConfidence: (T) -> Double? = { null },
    crossinline planetType: (T) -> String? = { null },
    crossinline planetImage: (T) -> ImageResource? = { null },
    crossinline planetRocheScore: (T) -> Double? = { null },
    crossinline planetHabitableZoneKopparapuScore: (T) -> Double? = { null },
    crossinline planetHabitableZoneKastingScore: (T) -> Double? = { null },
    crossinline planetRadiusScore: (T) -> Double? = { null },
    crossinline planetMassScore: (T) -> Double? = { null },
    crossinline planetTelluricityScore: (T) -> Double? = { null },
    crossinline planetEccentricityScore: (T) -> Double? = { null },
    crossinline planetTemperatureScore: (T) -> Double? = { null },
    crossinline planetObliquityScore: (T) -> Double? = { null },
    crossinline planetEsiScore: (T) -> Double? = { null },
    crossinline planetProtectionScore: (T) -> Double? = { null },
    crossinline planetTidalLockingScore: (T) -> Double? = { null },
    crossinline onPlanetClick: (T) -> Unit = {}
) {
    LazyColumnWithScrollBar(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        state = listState
    ) {
        if (showStellarHost) {
            item(key = stellarHostId) {
                StellarHostCard(
                    name = stellarHostName,
                    description = stellarHostDescription,
                    systemName = stellarHostSystemName,
                    planetCount = stellarHostPlanetCount,
                    spectralType = stellarHostSpectralType,
                    spectralImage = stellarHostSpectralImage,
                    effectiveTemperature = stellarHostEffectiveTemperature,
                    radius = stellarHostRadius,
                    mass = stellarHostMass,
                    metallicity = stellarHostMetallicity,
                    luminosity = stellarHostLuminosity,
                    gravity = stellarHostGravity,
                    age = stellarHostAge,
                    density = stellarHostDensity,
                    rotationalVelocity = stellarHostRotationalVelocity,
                    rotationalPeriod = stellarHostRotationalPeriod,
                    distance = stellarHostDistance,
                    ra = stellarHostRa,
                    dec = stellarHostDec,
                    spectralTypeScore = stellarHostSpectralTypeScore,
                    massScore = stellarHostMassScore,
                    ageScore = stellarHostAgeScore,
                    activityScore = stellarHostActivityScore,
                    rotationalPeriodScore = stellarHostRotationalPeriodScore,
                    gravityScore = stellarHostGravityScore,
                    metallicityScore = stellarHostMetallicityScore,
                    effectiveTemperatureScore = stellarHostEffectiveTemperatureScore
                )
            }
            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = planets, key = planetId) { planet ->
            PlanetCard(
                modifier = Modifier
                    .clickable { onPlanetClick(planet) },
                name = planetName(planet),
                description = planetDescription(planet),
                status = planetStatus(planet),
                orbitalPeriod = planetOrbitalPeriod(planet),
                orbitAxis = planetOrbitAxis(planet),
                radius = planetRadius(planet),
                mass = planetMass(planet),
                density = planetDensity(planet),
                eccentricity = planetEccentricity(planet),
                insolationFlux = planetInsolationFlux(planet),
                equilibriumTemperature = planetEquilibriumTemperature(planet),
                occultationDepth = planetOccultationDepth(planet),
                inclination = planetInclination(planet),
                obliquity = planetObliquity(planet),
                habitability = planetHabitability(planet),
                confidence = planetConfidence(planet),
                type = planetType(planet),
                image = planetImage(planet),
                rocheScore = planetRocheScore(planet),
                habitableZoneKopparapuScore = planetHabitableZoneKopparapuScore(planet),
                habitableZoneKastingScore = planetHabitableZoneKastingScore(planet),
                radiusScore = planetRadiusScore(planet),
                massScore = planetMassScore(planet),
                telluricityScore = planetTelluricityScore(planet),
                eccentricityScore = planetEccentricityScore(planet),
                temperatureScore = planetTemperatureScore(planet),
                obliquityScore = planetObliquityScore(planet),
                esiScore = planetEsiScore(planet),
                protectionScore = planetProtectionScore(planet),
                tidalLockingScore = planetTidalLockingScore(planet)
            )
        }
    }
}
