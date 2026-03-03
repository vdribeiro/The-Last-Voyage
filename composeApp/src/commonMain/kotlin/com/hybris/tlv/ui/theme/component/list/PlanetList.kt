package com.hybris.tlv.ui.theme.component.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.core.resource.ImageResource
import com.hybris.tlv.domain.usecase.space.model.PlanetType
import com.hybris.tlv.domain.usecase.space.spectralTypeToImage
import com.hybris.tlv.domain.usecase.space.toImage
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.divider.Divider

@Composable
internal fun <T> PlanetList(
    modifier: Modifier = Modifier,
    listState: LazyListState = LazyListState(),
    showStellarHost: Boolean = false,
    stellarHostName: String? = null,
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
    planets: ImmutableList<T> = persistentListOf(),
    planetId: (T) -> String = { it.hashCode().toString() },
    planetName: (T) -> String? = { null },
    planetStatus: (T) -> String? = { null },
    planetOrbitalPeriod: (T) -> Double? = { null },
    planetOrbitAxis: (T) -> Double? = { null },
    planetRadius: (T) -> Double? = { null },
    planetMass: (T) -> Double? = { null },
    planetDensity: (T) -> Double? = { null },
    planetEccentricity: (T) -> Double? = { null },
    planetInsolationFlux: (T) -> Double? = { null },
    planetEquilibriumTemperature: (T) -> Double? = { null },
    planetOccultationDepth: (T) -> Double? = { null },
    planetInclination: (T) -> Double? = { null },
    planetObliquity: (T) -> Double? = { null },
    planetHabitability: (T) -> Double? = { null },
    planetConfidence: (T) -> Double? = { null },
    planetType: (T) -> String? = { null },
    planetImage: (T) -> ImageResource? = { null },
    planetRocheScore: (T) -> Double? = { null },
    planetHabitableZoneKopparapuScore: (T) -> Double? = { null },
    planetHabitableZoneKastingScore: (T) -> Double? = { null },
    planetRadiusScore: (T) -> Double? = { null },
    planetMassScore: (T) -> Double? = { null },
    planetTelluricityScore: (T) -> Double? = { null },
    planetEccentricityScore: (T) -> Double? = { null },
    planetTemperatureScore: (T) -> Double? = { null },
    planetObliquityScore: (T) -> Double? = { null },
    planetEsiScore: (T) -> Double? = { null },
    planetProtectionScore: (T) -> Double? = { null },
    planetTidalLockingScore: (T) -> Double? = { null },
    onPlanetClick: (T) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.Start,
        state = listState
    ) {
        if (showStellarHost) {
            item {
                StellarHostCard(
                    name = stellarHostName,
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
            if (planets.isNotEmpty()) item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = planets, key = planetId) { planet ->
            PlanetCard(
                modifier = Modifier
                    .clickable { onPlanetClick(planet) },
                name = planetName(planet),
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

@Preview
@Composable
private fun PlanetListPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "stellar_host_system_name",
                value = "System Name"
            ),
            Translation(
                key = "stellar_host_planet_count",
                value = "Planet Count"
            ),
            Translation(
                key = "stellar_host_type",
                value = "Host"
            ),
            Translation(
                key = "planet_habitability",
                value = "Habitability"
            ),
            Translation(
                key = "planet_radius",
                value = "Radius"
            ),
            Translation(
                key = "planet_mass",
                value = "Mass"
            ),
        )
    )
    PlanetList(
        showStellarHost = true,
        stellarHostName = "Host",
        stellarHostSystemName = "System",
        stellarHostPlanetCount = 1,
        stellarHostSpectralType = "G",
        stellarHostSpectralImage = "G".spectralTypeToImage(),
        planets = persistentListOf(
            "Planet 1",
            "Planet 2",
            "Planet 3",
        ),
        planetName = { it },
        planetImage = { PlanetType.EARTH_ANALOG_PLANET.toImage() },
        planetHabitability = { 0.9 },
        planetRadius = { 1.0 },
        planetMass = { 1.0 }
    )
}