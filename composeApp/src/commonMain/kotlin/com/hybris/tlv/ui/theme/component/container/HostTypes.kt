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
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.space.spectralTypeToImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal inline fun <T> HostTypes(
    modifier: Modifier = Modifier,
    stellarHosts: List<T> = emptyList(),
    noinline stellarHostId: (T) -> String = { generateUuid() },
    crossinline stellarHostDescription: (T) -> String? = { null },
    crossinline stellarHostImage: (T) -> ImageResource? = { null },
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
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
        items(items = stellarHosts, key = stellarHostId) { stellarHost ->
            StellarHostCard(
                name = getTranslation(key = stellarHostId(stellarHost)),
                description = stellarHostDescription(stellarHost),
                spectralImage = stellarHostImage(stellarHost),
            )
        }
    }
}

@Preview
@Composable
private fun HostTypesPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__definition_types",
                value = "Types"
            ),
        )
    )
    HostTypes(
        stellarHosts = listOf(
            "Stellar Host 1",
            "Stellar Host 2",
            "Stellar Host 3",
        ),
        stellarHostId = { it },
        stellarHostDescription = { it },
        stellarHostImage = { "G".spectralTypeToImage() },
    )
}
