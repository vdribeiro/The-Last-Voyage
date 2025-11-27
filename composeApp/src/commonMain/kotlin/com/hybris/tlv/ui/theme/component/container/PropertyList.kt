package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal inline fun <T> PropertyList(
    modifier: Modifier = Modifier,
    title: String? = null,
    properties: List<T> = emptyList(),
    noinline id: (T) -> String = { generateUuid() },
    crossinline description: (T) -> String? = { null },
    crossinline image: (T) -> ImageResource? = { null },
    crossinline icon: (T) -> @Composable (() -> Unit)? = { null },
    noinline footer: @Composable (() -> Unit)? = null
) {
    val typography = LocalTypography.current

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        title?.let {
            item {
                Text(
                    text = it,
                    style = typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        items(items = properties, key = id) { property ->
            PropertyCard(
                name = id(property),
                description = description(property),
                image = image(property),
                icon = icon(property)
            )
        }

        if (footer != null) item { footer() }
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
    PropertyList(
        properties = listOf(
            "Property 1",
            "Property 2",
            "Property 3",
        ),
        id = { it },
        description = { it }
    )
}