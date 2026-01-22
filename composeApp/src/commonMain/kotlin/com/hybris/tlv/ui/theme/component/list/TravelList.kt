package com.hybris.tlv.ui.theme.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.core.security.generateUuid
import com.hybris.tlv.domain.usecase.space.spectralTypeToImage
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.infrastructure.resource.ImageResource
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.card.StellarHostCard

@Composable
internal inline fun <T> TravelList(
    modifier: Modifier = Modifier,
    stellarHosts: List<T> = emptyList(),
    noinline id: (T) -> String = { generateUuid() },
    crossinline name: (T) -> String? = { null },
    crossinline planetCount: (T) -> Int? = { null },
    crossinline spectralType: (T) -> String? = { null },
    crossinline spectralImage: (T) -> ImageResource? = { null },
    crossinline distance: (T) -> Double? = { null },
    crossinline onClick: (T) -> Unit = {},
    noinline footer: (@Composable () -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        items(items = stellarHosts, key = id) { stellarHost ->
            StellarHostCard(
                modifier = Modifier
                    .clickable { onClick(stellarHost) },
                name = name(stellarHost),
                planetCount = planetCount(stellarHost),
                spectralType = spectralType(stellarHost),
                spectralImage = spectralImage(stellarHost),
                distance = distance(stellarHost),
            )
        }

        if (footer != null) item { footer() }
    }
}

@Preview
@Composable
private fun TravelListPreview() = AppTheme {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "stellar_host_planet_count",
                value = "Planet Count"
            ),
            Translation(
                key = "stellar_host_type",
                value = "Host"
            ),
            Translation(
                key = "stellar_host_distance",
                value = "Distance"
            )
        )
    )
    TravelList(
        stellarHosts = listOf(
            "Host 1",
            "Host 2",
            "Host 3",
        ),
        name = { it },
        planetCount = { 2 },
        spectralType = { "A" },
        spectralImage = { "A".spectralTypeToImage() },
        distance = { 10.0 },
    )
}