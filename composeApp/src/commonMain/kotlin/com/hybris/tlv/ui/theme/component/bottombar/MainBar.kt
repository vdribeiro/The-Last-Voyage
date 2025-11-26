package com.hybris.tlv.ui.theme.component.bottombar

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.platform.isIos
import com.hybris.tlv.platform.open
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.Image
import com.hybris.tlv.ui.theme.component.image.ImageResource
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.kofi

@Composable
internal fun MainBar(
    modifier: Modifier = Modifier,
    onCreditsClick: () -> Unit = {},
    developerCornerUri: String? = null,
    supportUri: String? = null,
) {
    val uriHandler = LocalUriHandler.current
    val websiteTranslation = getTranslation(key = "website")
    val creditsTranslation = getTranslation(key = "main_menu_screen__credits")

    val typography = LocalTypography.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .size(size = 100.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable { uriHandler.open(uri = developerCornerUri) },
            text = websiteTranslation,
            style = typography.labelLarge,
        )
        if (!isIos) {
            Image(
                modifier = Modifier
                    .size(size = 100.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .clickable { uriHandler.open(uri = supportUri) },
                image = ImageResource(
                    path = "kofi.png",
                    drawable = Res.drawable.kofi
                ),
                contentDescription = "Support",
                contentScale = ContentScale.Fit,
            )
        }
        Text(
            modifier = Modifier
                .size(size = 100.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable(onClick = onCreditsClick),
            text = creditsTranslation,
            style = typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun MainBarPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "website",
                value = "Website with large description"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            )
        )
    )
    MainBar()
}
