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
import com.hybris.tlv.data.resource.ImageResource
import com.hybris.tlv.domain.space.PlanetType
import com.hybris.tlv.domain.usecase.space.spectralTypeToImage
import com.hybris.tlv.domain.usecase.space.toImage
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.divider.Divider

@Composable
internal fun <H, P> ExoplanetList(
    modifier: Modifier = Modifier,
    listState: LazyListState = LazyListState(),
    hostsFirst: Boolean = true,
    stellarHosts: ImmutableList<H> = persistentListOf(),
    stellarHostId: (H) -> String = { it.hashCode().toString() },
    stellarHostName: (H) -> String? = { null },
    stellarHostSystemName: (H) -> String? = { null },
    stellarHostPlanetCount: (H) -> Int? = { null },
    stellarHostSpectralType: (H) -> String? = { null },
    stellarHostSpectralImage: (H) -> ImageResource? = { null },
    stellarHostEffectiveTemperature: (H) -> Double? = { null },
    stellarHostRadius: (H) -> Double? = { null },
    stellarHostMass: (H) -> Double? = { null },
    stellarHostMetallicity: (H) -> Double? = { null },
    stellarHostLuminosity: (H) -> Double? = { null },
    stellarHostGravity: (H) -> Double? = { null },
    stellarHostAge: (H) -> Double? = { null },
    stellarHostDensity: (H) -> Double? = { null },
    stellarHostRotationalVelocity: (H) -> Double? = { null },
    stellarHostRotationalPeriod: (H) -> Double? = { null },
    stellarHostDistance: (H) -> Double? = { null },
    stellarHostRa: (H) -> Double? = { null },
    stellarHostDec: (H) -> Double? = { null },
    stellarHostSpectralTypeScore: (H) -> Double? = { null },
    stellarHostMassScore: (H) -> Double? = { null },
    stellarHostAgeScore: (H) -> Double? = { null },
    stellarHostActivityScore: (H) -> Double? = { null },
    stellarHostRotationalPeriodScore: (H) -> Double? = { null },
    stellarHostGravityScore: (H) -> Double? = { null },
    stellarHostMetallicityScore: (H) -> Double? = { null },
    stellarHostEffectiveTemperatureScore: (H) -> Double? = { null },
    onStellarHostClick: (H) -> Unit = {},
    planets: ImmutableList<P> = persistentListOf(),
    planetId: (P) -> String = { it.hashCode().toString() },
    planetName: (P) -> String? = { null },
    planetStatus: @Composable (P) -> String? = { null },
    planetOrbitalPeriod: (P) -> Double? = { null },
    planetOrbitAxis: (P) -> Double? = { null },
    planetRadius: (P) -> Double? = { null },
    planetMass: (P) -> Double? = { null },
    planetDensity: (P) -> Double? = { null },
    planetEccentricity: (P) -> Double? = { null },
    planetInsolationFlux: (P) -> Double? = { null },
    planetEquilibriumTemperature: (P) -> Double? = { null },
    planetOccultationDepth: (P) -> Double? = { null },
    planetInclination: (P) -> Double? = { null },
    planetObliquity: (P) -> Double? = { null },
    planetHabitability: (P) -> Double? = { null },
    planetConfidence: (P) -> Double? = { null },
    planetType: @Composable (P) -> String? = { null },
    planetImage: (P) -> ImageResource? = { null },
    planetRocheScore: (P) -> Double? = { null },
    planetHabitableZoneKopparapuScore: (P) -> Double? = { null },
    planetHabitableZoneKastingScore: (P) -> Double? = { null },
    planetRadiusScore: (P) -> Double? = { null },
    planetMassScore: (P) -> Double? = { null },
    planetTelluricityScore: (P) -> Double? = { null },
    planetEccentricityScore: (P) -> Double? = { null },
    planetTemperatureScore: (P) -> Double? = { null },
    planetObliquityScore: (P) -> Double? = { null },
    planetEsiScore: (P) -> Double? = { null },
    planetProtectionScore: (P) -> Double? = { null },
    planetTidalLockingScore: (P) -> Double? = { null },
    onPlanetClick: (P) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.Start,
        state = listState
    ) {
        val hostItems = {
            items(items = stellarHosts, key = stellarHostId) { stellarHost ->
                StellarHostCard(
                    modifier = Modifier.clickable { onStellarHostClick(stellarHost) },
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
        val planetItems = {
            items(items = planets, key = planetId) { planet ->
                PlanetCard(
                    modifier = Modifier.clickable { onPlanetClick(planet) },
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
        if (hostsFirst) {
            hostItems()
            if (planets.isNotEmpty()) item { Divider(modifier = Modifier.animateItem().padding(vertical = 8.dp)) }
            planetItems()
        } else {
            planetItems()
            if (stellarHosts.isNotEmpty()) item { Divider(modifier = Modifier.animateItem().padding(vertical = 8.dp)) }
            hostItems()
        }
    }
}

@Preview
@Composable
private fun ExoplanetListPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "stellar_host_system_name",
                value = "System Name"
            ),
            PreviewTranslation(
                key = "stellar_host_planet_count",
                value = "Planet Count"
            ),
            PreviewTranslation(
                key = "stellar_host_type",
                value = "Host"
            ),
            PreviewTranslation(
                key = "stellar_host_temperature",
                value = "Temperature"
            ),
            PreviewTranslation(
                key = "planet_habitability",
                value = "Habitability"
            ),
            PreviewTranslation(
                key = "planet_radius",
                value = "Radius"
            ),
            PreviewTranslation(
                key = "planet_mass",
                value = "Mass"
            )
        )
    )
    ExoplanetList(
        stellarHosts = persistentListOf(
            "Host 1",
            "Host 2",
            "Host 3",
        ),
        stellarHostName = { it },
        stellarHostPlanetCount = { 1 },
        stellarHostSpectralType = { "G" },
        stellarHostSpectralImage = { "G".spectralTypeToImage() },
        stellarHostEffectiveTemperature = { 321.0 },
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