package com.hybris.tlv.ui.theme.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.core.resource.ImageResource
import com.hybris.tlv.domain.usecase.space.spectralTypeToImage
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.image.Image
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun PropertyCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    leadingImage: ImageResource? = null,
    icon: (@Composable () -> Unit)? = null,
) {
    val typography = LocalTypography.current

    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            leadingImage?.let {
                Image(
                    modifier = Modifier
                        .size(size = 72.dp)
                        .clip(shape = shapes.small)
                        .align(alignment = Alignment.Top),
                    image = it,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
            Column(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    name?.let { Text(modifier = Modifier.weight(1f, fill = false), text = getTranslation(key = it), style = typography.titleLarge, fontWeight = FontWeight.Bold) }
                    if (name != null && icon != null) Spacer(modifier = Modifier.width(width = 8.dp))
                    icon?.let { it() }
                }
                if (name != null && description != null) Spacer(modifier = Modifier.height(height = 4.dp))
                description?.let { Text(text = getTranslation(key = it), style = typography.bodyLarge) }
            }
        }
    }
}

@Preview
@Composable
private fun PropertyCardPreview() = Preview {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        PropertyCard(
            name = "Property",
            description = "Hammer Time",
            leadingImage = "W".spectralTypeToImage(),
            icon = { Icon(imageVector = Icons.Filled.Apps) },
        )
        PropertyCard(
            name = "Very very long and large name of this property name",
            description = "Very very long and large name of this property description",
            leadingImage = "W".spectralTypeToImage(),
            icon = { Icon(imageVector = Icons.Filled.Apps) },
        )
        PropertyCard(
            name = "Property"
        )
        PropertyCard(
            description = "Hammer Time"
        )
        PropertyCard(
            leadingImage = "W".spectralTypeToImage(),
        )
        PropertyCard(
            icon = { Icon(imageVector = Icons.Filled.Apps) },
        )
        PropertyCard(
            name = "Property",
            description = "Hammer Time",
        )
        PropertyCard(
            description = "Hammer Time",
            leadingImage = "W".spectralTypeToImage(),
        )
    }
}
