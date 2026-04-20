package com.hybris.tlv.ui.theme.component.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.data.resource.ImageResource
import com.hybris.tlv.domain.usecase.space.spectralTypeToImage
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text

@Composable
internal fun <T> PropertyList(
    modifier: Modifier = Modifier,
    title: String? = null,
    properties: ImmutableList<T> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    name: @Composable (T) -> String? = { null },
    description: @Composable (T) -> String? = { null },
    leadingImage: (T) -> ImageResource? = { null },
    icon: (T) -> (@Composable () -> Unit)? = { null },
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null
) {
    val typography = LocalTypography.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = title,
            style = typography.headlineMedium,
        )
        if (header != null) header()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        ) {
            items(items = properties, key = id) { property ->
                PropertyCard(
                    name = name(property),
                    description = description(property),
                    leadingImage = leadingImage(property),
                    icon = icon(property),
                )
            }

            if (footer != null) item { footer() }
        }
    }
}

@Preview
@Composable
private fun PlanetDefinitionPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "main_menu_screen__planet_definition",
                value = "Definition"
            ),
        )
    )
    PropertyList(
        properties = persistentListOf(
            "Property 1",
            "Property 2",
            "Property 3",
        ),
        id = { it },
        name = { it },
        description = { it },
        leadingImage = { "W".spectralTypeToImage() },
        icon = { { Icon(imageVector = Icons.Filled.Apps) } },
    )
}
