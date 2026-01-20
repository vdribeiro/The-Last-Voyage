package com.hybris.tlv.ui.theme.component.bottombar

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.platform.Property
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.dialog.Dialog
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun HelpBar(
    modifier: Modifier = Modifier,
    version: String = Property.APP_VERSION,
    onVersionClick: () -> Unit = {},
    onResetClick: () -> Unit = {},
) {
    val versionTranslation = getTranslation(key = "version")
    val resetTranslation = getTranslation(key = "reset")
    val resetConfirmTranslation = getTranslation(key = "reset_confirm")

    val typography = LocalTypography.current

    var reset: Boolean by remember { mutableStateOf(value = false) }

    if (reset) {
        Dialog(
            text = resetConfirmTranslation,
            onConfirm = {
                reset = false
                onResetClick()
            },
            onDismiss = { reset = false },
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .size(size = 100.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable(onClick = onVersionClick),
            text = "$versionTranslation: $version",
            textAlign = TextAlign.Start,
            style = typography.labelLarge,
        )
        Text(
            modifier = Modifier
                .size(size = 100.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable { reset = true },
            text = resetTranslation,
            textAlign = TextAlign.End,
            style = typography.labelLarge,
        )
    }
}

@Preview
@Composable
private fun HelpBarPreview() = AppTheme {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "version",
                value = "Version"
            ),
            Translation(
                key = "reset",
                value = "Reset"
            )
        )
    )
    HelpBar(

    )
}
