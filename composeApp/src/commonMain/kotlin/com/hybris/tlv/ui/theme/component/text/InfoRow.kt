package com.hybris.tlv.ui.theme.component.text

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography

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
            style = typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value.toString(),
            style = typography.bodyLarge,
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
