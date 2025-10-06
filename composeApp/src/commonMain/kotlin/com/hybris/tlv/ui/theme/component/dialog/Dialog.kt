package com.hybris.tlv.ui.theme.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.text.Text
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Dialog(
    modifier: Modifier = Modifier,
    title: String = "",
    confirmText: String = "",
    dismissText: String = "",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = title) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = dismissText)
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@Preview
@Composable
private fun DialogPreview() = AppTheme {
    Dialog()
}
