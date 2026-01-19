package com.hybris.tlv.ui.theme.component.dialog

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.ui.translation.TranslationCache

@Composable
internal fun Dialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    confirmText: String? = null,
    dismissText: String? = null,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onDismissRequest: () -> Unit = onDismiss,
) {
    val typography = LocalTypography.current

    val confirmText: String = confirmText ?: getTranslation(key = "app_yes")
    val dismissText: String = dismissText ?: getTranslation(key = "app_no")
    AlertDialog(
        modifier = modifier,
        title = {
            title?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Start,
                    style = typography.titleLarge
                )
            }
        },
        text = {
            text?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Start,
                    style = typography.bodyLarge
                )
            }
        },
        confirmButton = { Button(text = confirmText, onClick = onConfirm) },
        dismissButton = { Button(text = dismissText, onClick = onDismiss) },
        onDismissRequest = onDismissRequest
    )
}

@Preview
@Composable
private fun DialogPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_yes",
                value = "Yes"
            ),
            Translation(
                key = "app_no",
                value = "No"
            )
        )
    )
    Dialog(
        title = "Title",
        text = "Text"
    )
}
