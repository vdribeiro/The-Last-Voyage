package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hybris.tlv.core.security.generateUuid
import com.hybris.tlv.resource.ImageResource
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.domain.usecase.translation.model.Translation

@Composable
internal inline fun <T> PropertyList(
    modifier: Modifier = Modifier,
    title: String? = null,
    properties: List<T> = emptyList(),
    noinline id: (T) -> String = { generateUuid() },
    crossinline description: (T) -> String? = { null },
    crossinline leadingImage: (T) -> ImageResource? = { null },
    crossinline icon: (T) -> (@Composable () -> Unit)? = { null },
    crossinline trailingIcon: (T) -> ImageVector? = { null },
    noinline header: (@Composable () -> Unit)? = null,
    noinline footer: (@Composable () -> Unit)? = null
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
                    name = id(property),
                    description = description(property),
                    leadingImage = leadingImage(property),
                    icon = icon(property),
                    trailingIcon = trailingIcon(property)
                )
            }

            if (footer != null) item { footer() }
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
