package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal inline fun <T, P> PlanetDefinition(
    modifier: Modifier = Modifier,
    name: String? = null,
    status: String? = null,
    orbitalPeriod: Double? = null,
    orbitAxis: Double? = null,
    radius: Double? = null,
    mass: Double? = null,
    density: Double? = null,
    eccentricity: Double? = null,
    insolationFlux: Double? = null,
    equilibriumTemperature: Double? = null,
    occultationDepth: Double? = null,
    inclination: Double? = null,
    obliquity: Double? = null,
    type: String? = null,
    image: ImageResource? = null,
    properties: List<T> = emptyList(),
    noinline propertyId: (T) -> String = { "" },
    crossinline propertyDescription: (T) -> String? = { null },
    planets: List<P> = emptyList(),
    noinline planetId: (P) -> String = { "" },
    crossinline planetDescription: (P) -> String? = { null },
    crossinline planetImage: (P) -> ImageResource? = { null },
) {
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val exampleTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_example") }
    val propertiesTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_properties") }
    val typesTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__definition_types") }

    val typography = LocalTypography.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            Text(
                text = exampleTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        item {
            PlanetCard(
                name = name,
                status = status,
                orbitalPeriod = orbitalPeriod,
                orbitAxis = orbitAxis,
                radius = radius,
                mass = mass,
                density = density,
                eccentricity = eccentricity,
                insolationFlux = insolationFlux,
                equilibriumTemperature = equilibriumTemperature,
                occultationDepth = occultationDepth,
                inclination = inclination,
                obliquity = obliquity,
                type = type,
                image = image
            )
        }
        item {
            Text(
                text = propertiesTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = properties, key = propertyId) { property ->
            PropertyCard(
                name = getTranslation(key = propertyId(property)),
                description = propertyDescription(property)?.let { getTranslation(key = it) },
            )
        }
        item {
            Text(
                text = typesTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = planets, key = planetId) { planet ->
            PlanetCard(
                name = getTranslation(key = planetId(planet)),
                description = planetDescription(planet),
                image = planetImage(planet)
            )
        }
    }
}