package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hybris.tlv.ui.theme.typography

@Composable
internal fun InfoRow(
    modifier: Modifier = Modifier,
    label: String,
    value: Any?
) {
    Row(modifier = modifier) {
        Text(
            text = "$label: ",
            style = typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value.toString(),
            style = typography.bodyMedium
        )
    }
}