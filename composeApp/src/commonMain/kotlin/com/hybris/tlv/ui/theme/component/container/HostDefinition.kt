package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal inline fun <T> HostDefinition(
    modifier: Modifier = Modifier,
    name: String? = null,
    systemName: String? = null,
    planetCount: Int? = null,
    spectralType: String? = null,
    spectralImage: ImageResource? = null,
    effectiveTemperature: Double? = null,
    radius: Double? = null,
    mass: Double? = null,
    metallicity: Double? = null,
    luminosity: Double? = null,
    gravity: Double? = null,
    age: Double? = null,
    density: Double? = null,
    rotationalVelocity: Double? = null,
    rotationalPeriod: Double? = null,
    distance: Double? = null,
    ra: Double? = null,
    dec: Double? = null,
    properties: List<T> = emptyList(),
    noinline propertyId: (T) -> String = { generateUuid() },
    crossinline propertyDescription: (T) -> String? = { null },
) {
    val definitionTranslation = getTranslation(key = "main_menu_screen__host_definition")

    val typography = LocalTypography.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            Text(
                text = definitionTranslation,
                style = typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        item {
            StellarHostCard(
                name = name,
                systemName = systemName,
                planetCount = planetCount,
                spectralType = spectralType,
                spectralImage = spectralImage,
                effectiveTemperature = effectiveTemperature,
                radius = radius,
                mass = mass,
                metallicity = metallicity,
                luminosity = luminosity,
                gravity = gravity,
                age = age,
                density = density,
                rotationalVelocity = rotationalVelocity,
                rotationalPeriod = rotationalPeriod,
                distance = distance,
                ra = ra,
                dec = dec,
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
        }
        items(items = properties, key = propertyId) { property ->
            PropertyCard(
                name = getTranslation(key = propertyId(property)),
                description = propertyDescription(property)?.let { getTranslation(key = it) }
            )
        }
    }
}

@Preview
@Composable
private fun HostDefinitionPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__host_definition",
                value = "Definition"
            ),
        )
    )
    HostDefinition(
        name = "Sun",
        properties = listOf(
            "Property 1",
            "Property 2",
            "Property 3",
        ),
        propertyId = { it },
        propertyDescription = { it },
    )
}
