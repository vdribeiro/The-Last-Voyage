package com.hybris.tlv.ui.theme.component.text

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun InfoRow(
    modifier: Modifier = Modifier,
    label: String = "",
    value: Any? = null
) {
    val typography = LocalTypography.current

    Row(modifier = modifier) {
        Text(
            text = "$label: ",
            style = typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = value.toString(),
            style = typography.bodyMedium,
            maxLines = 1
        )
    }
}

@Preview
@Composable
private fun InfoRowPreview() = AppTheme {
    InfoRow(
        label = "Label",
        value = "Value"
    )
}
