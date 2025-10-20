package com.hybris.tlv.ui.theme.component.dialog

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun Dialog(
    modifier: Modifier = Modifier,
    title: String = "",
    confirmText: String = getTranslation(key = "app_yes"),
    dismissText: String = getTranslation(key = "app_no"),
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val typography = LocalTypography.current

    AlertDialog(
        modifier = modifier,
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = typography.titleLarge
            )
        },
        confirmButton = { Button(text = confirmText, onClick = onConfirm) },
        dismissButton = { Button(text = dismissText, onClick = onDismiss) },
        onDismissRequest = onDismissRequest
    )
}

@Preview
@Composable
private fun DialogPreview() = AppTheme {
    Dialog(title = "Title")
}
