package com.hybris.tlv.ui.theme.component.list

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.dialog.Dialog
import com.hybris.tlv.ui.theme.component.divider.Divider
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.spectralTypeToImage
import com.hybris.tlv.usecase.space.toImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal inline fun <T> SystemList(
    modifier: Modifier = Modifier,
    stellarHostName: String? = null,
    stellarHostSpectralType: String? = null,
    stellarHostSpectralImage: ImageResource? = null,
    stellarHostEffectiveTemperature: Double? = null,
    stellarHostRadius: Double? = null,
    stellarHostMass: Double? = null,
    stellarHostAge: Double? = null,
    planets: List<T> = emptyList(),
    noinline planetId: (T) -> String = { generateUuid() },
    crossinline planetName: (T) -> String? = { null },
    crossinline planetRadius: (T) -> Double? = { null },
    crossinline planetMass: (T) -> Double? = { null },
    crossinline planetDensity: (T) -> Double? = { null },
    crossinline planetEquilibriumTemperature: (T) -> Double? = { null },
    crossinline planetHabitability: (T) -> Double? = { null },
    crossinline planetType: (T) -> String? = { null },
    crossinline planetImage: (T) -> ImageResource? = { null },
    crossinline onClick: (T) -> Unit = {}
) {
    var planetToSettle: T? by remember { mutableStateOf(value = null) }

    planetToSettle?.let {
        Dialog(
            title = getTranslation(key = "game_screen__settle", planetName(it).orEmpty()),
            onConfirm = { onClick(it) },
            onDismiss = { planetToSettle = null },
            onDismissRequest = { planetToSettle = null },
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            StellarHostCard(
                name = stellarHostName,
                spectralType = stellarHostSpectralType,
                spectralImage = stellarHostSpectralImage,
                effectiveTemperature = stellarHostEffectiveTemperature,
                radius = stellarHostRadius,
                mass = stellarHostMass,
                age = stellarHostAge,
            )
        }
        item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
        items(items = planets, key = planetId) { planet ->
            PlanetCard(
                modifier = Modifier
                    .clickable { planetToSettle = planet },
                name = planetName(planet),
                radius = planetRadius(planet),
                mass = planetMass(planet),
                density = planetDensity(planet),
                equilibriumTemperature = planetEquilibriumTemperature(planet),
                habitability = planetHabitability(planet),
                type = planetType(planet),
                image = planetImage(planet)
            )
        }
    }
}

@Preview
@Composable
private fun SystemListPreview() = AppTheme {
    TranslationCache.set(
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
                key = "stellar_host_type",
                value = "Host"
            ),
            Translation(
                key = "stellar_host_temperature",
                value = "Temperature"
            )
        )
    )
    SystemList(
        stellarHostName = "Host",
        stellarHostSpectralType = "G",
        stellarHostSpectralImage = "G".spectralTypeToImage(),
        stellarHostEffectiveTemperature = 4321.0,
        planets = listOf(
            "Planet 1",
            "Planet 2",
            "Planet 3",
        ),
        planetName = { it },
        planetImage = { PlanetType.EARTH_ANALOG_PLANET.toImage() },
        planetHabitability = { 0.9 },
        planetRadius = { 1.0 },
        planetMass = { 1.0 },
    )
}
