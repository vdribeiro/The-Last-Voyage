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
internal fun <T> StellarHostList(
    modifier: Modifier = Modifier,
    listState: LazyListState = LazyListState(),
    showPlanet: Boolean = false,
    planetName: String? = null,
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
    stellarHosts: ImmutableList<T> = persistentListOf(),
    stellarHostId: (T) -> String = { it.hashCode().toString() },
    stellarHostName: (T) -> String? = { null },
    stellarHostSystemName: (T) -> String? = { null },
    stellarHostPlanetCount: (T) -> Int? = { null },
    stellarHostSpectralType: (T) -> String? = { null },
    stellarHostSpectralImage: (T) -> ImageResource? = { null },
    stellarHostEffectiveTemperature: (T) -> Double? = { null },
    stellarHostRadius: (T) -> Double? = { null },
    stellarHostMass: (T) -> Double? = { null },
    stellarHostMetallicity: (T) -> Double? = { null },
    stellarHostLuminosity: (T) -> Double? = { null },
    stellarHostGravity: (T) -> Double? = { null },
    stellarHostAge: (T) -> Double? = { null },
    stellarHostDensity: (T) -> Double? = { null },
    stellarHostRotationalVelocity: (T) -> Double? = { null },
    stellarHostRotationalPeriod: (T) -> Double? = { null },
    stellarHostDistance: (T) -> Double? = { null },
    stellarHostRa: (T) -> Double? = { null },
    stellarHostDec: (T) -> Double? = { null },
    stellarHostSpectralTypeScore: (T) -> Double? = { null },
    stellarHostMassScore: (T) -> Double? = { null },
    stellarHostAgeScore: (T) -> Double? = { null },
    stellarHostActivityScore: (T) -> Double? = { null },
    stellarHostRotationalPeriodScore: (T) -> Double? = { null },
    stellarHostGravityScore: (T) -> Double? = { null },
    stellarHostMetallicityScore: (T) -> Double? = { null },
    stellarHostEffectiveTemperatureScore: (T) -> Double? = { null },
    onStellarHostClick: (T) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.Start,
        state = listState
    ) {
        if (showPlanet) {
            item {
                PlanetCard(
                    name = planetName,
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
            if (stellarHosts.isNotEmpty()) item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
        }
        items(items = stellarHosts, key = stellarHostId) { stellarHost ->
            StellarHostCard(
                modifier = Modifier
                    .clickable { onStellarHostClick(stellarHost) },
                name = stellarHostName(stellarHost),
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

@Preview
@Composable
private fun StellarHostListPreview() = Preview {
    InjectTranslations(
        translations = listOf(
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
            Translation(
                key = "stellar_host_planet_count",
                value = "Planet Count"
            ),
            Translation(
                key = "stellar_host_type",
                value = "Host"
            ),
            Translation(
                key = "stellar_host_temperature",
                value = "Temperature"
            )
        )
    )
    StellarHostList(
        showPlanet = true,
        planetName = "Planet",
        planetImage = PlanetType.EARTH_ANALOG_PLANET.toImage(),
        planetHabitability = 0.9,
        planetRadius = 1.0,
        planetMass = 1.0,
        stellarHosts = persistentListOf(
            "Host 1",
            "Host 2",
            "Host 3",
        ),
        stellarHostName = { it },
        stellarHostPlanetCount = { 1 },
        stellarHostSpectralType = { "G" },
        stellarHostSpectralImage = { "G".spectralTypeToImage() },
        stellarHostEffectiveTemperature = { 321.0 }
    )
}