package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.Card
import com.hybris.tlv.ui.theme.component.image.Image
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.InfoRow
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.space.spectralTypeToImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal inline fun <T> PropertiesDefinition(
    modifier: Modifier = Modifier,
    image: ImageResource? = null,
    properties: List<T> = emptyList(),
    noinline propertyId: (T) -> String = { generateUuid() },
    crossinline propertyDescription: (T) -> String? = { null }
) {
    val definitionTranslation = getTranslation(key = "main_menu_screen__planet_definition")

    val typography = LocalTypography.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = definitionTranslation,
            style = typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))

        Card(modifier = modifier) {
            Row(
                modifier = Modifier
                    .padding(all = 12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                image?.let {
                    Image(
                        modifier = Modifier
                            .size(size = 72.dp)
                            .clip(shape = shapes.small)
                            .align(alignment = Alignment.Top),
                        image = it,
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(width = 16.dp))
                }
                LazyColumn(
                    modifier = Modifier.weight(weight = 1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    items(items = properties, key = propertyId) { property ->
                        InfoRow(label = getTranslation(key = propertyId(property)), value = propertyDescription(property)?.let { getTranslation(key = it) })
                    }
                }
                Spacer(modifier = Modifier.weight(weight = 0.1f))
            }
        }
    }
}

@Preview
@Composable
private fun PlanetDefinitionPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__planet_definition",
                value = "Definition"
            ),
        )
    )
    PropertiesDefinition(
        image = "L".spectralTypeToImage(),
        properties = listOf(
            "Property 1",
            "Property 2",
            "Property 3",
        ),
        propertyId = { it },
        propertyDescription = { it }
    )
}