package com.hybris.tlv.ui.theme.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    AlertDialog(
        modifier = modifier,
        title = { Text(text = title) },
        confirmButton = { Button(text = confirmText, onClick = onConfirm) },
        dismissButton = { Button(text = dismissText, onClick = onDismiss) },
        onDismissRequest = onDismissRequest
    )
}

@Preview
@Composable
private fun DialogPreview() = AppTheme {
    Dialog()
}
