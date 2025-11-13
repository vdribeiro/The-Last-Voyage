package com.hybris.tlv.ui.theme.component.list

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.dialog.Dialog
import com.hybris.tlv.ui.theme.component.divider.Divider
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun <T> SystemList(
    modifier: Modifier = Modifier,
    stellarHostId: String? = null,
    stellarHostName: String? = null,
    stellarHostSpectralType: String? = null,
    stellarHostSpectralImage: ImageResource? = null,
    stellarHostEffectiveTemperature: Double? = null,
    stellarHostRadius: Double? = null,
    stellarHostMass: Double? = null,
    stellarHostAge: Double? = null,
    planets: List<T> = emptyList(),
    planetId: (T) -> String = { "" },
    planetName: (T) -> String? = { null },
    planetRadius: (T) -> Double? = { null },
    planetMass: (T) -> Double? = { null },
    planetDensity: (T) -> Double? = { null },
    planetEquilibriumTemperature: (T) -> Double? = { null },
    planetHabitability: (T) -> Double? = { null },
    planetType: (T) -> String? = { null },
    planetImage: (T) -> ImageResource? = { null },
    onClick: (T) -> Unit = {}
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

    LazyColumnWithScrollBar(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        item(key = stellarHostId) {
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
