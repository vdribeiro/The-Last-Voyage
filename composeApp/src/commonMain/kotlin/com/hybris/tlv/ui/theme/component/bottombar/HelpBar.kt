package com.hybris.tlv.ui.theme.component.bottombar

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.platform.Property
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun HelpBar(
    modifier: Modifier = Modifier,
    version: String = Property.APP_VERSION,
) {
    val versionTranslation = getTranslation(key = "version")

    val typography = LocalTypography.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .size(size = 100.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            text = "$versionTranslation: $version",
            style = typography.labelLarge,
        )
    }
}

@Preview
@Composable
private fun HelpBarPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "version",
                value = "Version"
            )
        )
    )
    HelpBar()
}
