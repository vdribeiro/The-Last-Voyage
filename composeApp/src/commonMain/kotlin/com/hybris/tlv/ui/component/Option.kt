package com.hybris.tlv.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun Option(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(height = 16.dp))
    Text(
        modifier = modifier.clickable(onClick = onClick),
        text = text,
        style = MaterialTheme.typography.headlineMedium,
    )
}
