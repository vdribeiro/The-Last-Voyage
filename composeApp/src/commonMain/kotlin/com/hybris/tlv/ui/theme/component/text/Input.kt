package com.hybris.tlv.ui.theme.component.text

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Input(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    maxLines: Int = Int.MAX_VALUE,
) {
    OutlinedTextField(
        modifier = modifier,
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        maxLines = maxLines
    )
}

@Preview
@Composable
private fun InputPreview() = AppTheme {
    Input()
}