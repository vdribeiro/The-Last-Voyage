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
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.toImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal inline fun <T> PlanetTypes(
    modifier: Modifier = Modifier,
    planets: List<T> = emptyList(),
    noinline planetId: (T) -> String = { generateUuid() },
    crossinline planetDescription: (T) -> String? = { null },
    crossinline planetImage: (T) -> ImageResource? = { null },
) {
    val typesTranslation = getTranslation(key = "main_menu_screen__definition_types")

    val typography = LocalTypography.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
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

@Preview
@Composable
private fun PlanetTypesPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__definition_types",
                value = "Types"
            ),
        )
    )
    PlanetTypes(
        planets = listOf(
            "Planet 1",
            "Planet 2",
            "Planet 3",
        ),
        planetId = { it },
        planetDescription = { it },
        planetImage = { PlanetType.EARTH_ANALOG_PLANET.toImage() },
    )
}