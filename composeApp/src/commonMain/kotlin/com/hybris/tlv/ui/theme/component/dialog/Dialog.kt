package com.hybris.tlv.ui.theme.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun Dialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    confirmText: String = getTranslation(key = "app_yes"),
    dismissText: String? = getTranslation(key = "app_no"),
    onConfirm: () -> Unit = {},
    onDismiss: (() -> Unit) = {},
    onDismissRequest: () -> Unit = onDismiss,
) {
    val typography = LocalTypography.current

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
        dismissButton = dismissText?.let {
            { Button(text = it, onClick = onDismiss) }
        },
        onDismissRequest = onDismissRequest
    )
}

@Preview
@Composable
private fun DialogPreview() = Preview {
    InjectTranslations(
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
