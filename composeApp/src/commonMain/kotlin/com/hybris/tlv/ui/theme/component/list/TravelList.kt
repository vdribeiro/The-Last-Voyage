package com.hybris.tlv.ui.theme.component.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.data.resource.ImageResource
import com.hybris.tlv.domain.usecase.space.spectralTypeToImage
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.theme.component.card.StellarHostCard

@Composable
internal fun <T> TravelList(
    modifier: Modifier = Modifier,
    stellarHosts: ImmutableList<T> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    name: (T) -> String? = { null },
    planetCount: (T) -> Int? = { null },
    spectralType: (T) -> String? = { null },
    spectralImage: (T) -> ImageResource? = { null },
    distance: (T) -> Double? = { null },
    onClick: (T) -> Unit = {},
    footer: (@Composable () -> Unit)? = null
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
private fun TravelListPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "stellar_host_planet_count",
                value = "Planet Count"
            ),
            PreviewTranslation(
                key = "stellar_host_type",
                value = "Host"
            ),
            PreviewTranslation(
                key = "stellar_host_distance",
                value = "Distance"
            )
        )
    )
    TravelList(
        stellarHosts = persistentListOf(
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