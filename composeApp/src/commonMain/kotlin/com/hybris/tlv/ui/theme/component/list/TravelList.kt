package com.hybris.tlv.ui.theme.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.image.ImageResource

@Composable
internal fun <T> TravelList(
    modifier: Modifier = Modifier,
    stellarHosts: List<T> = emptyList(),
    id: (T) -> String = { "" },
    name: (T) -> String? = { null },
    planetCount: (T) -> Int? = { null },
    spectralType: (T) -> String? = { null },
    spectralImage: (T) -> ImageResource? = { null },
    distance: (T) -> Double? = { null },
    onClick: (T) -> Unit = {},
) {
    LazyColumnWithScrollBar(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
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
    }
}