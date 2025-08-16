package com.hybris.tlv.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
internal fun CreditsText(
    modifier: Modifier = Modifier,
    name: String,
    link: String?,
) {
    val uriHandler = LocalUriHandler.current

    Spacer(modifier = Modifier.height(height = 8.dp))
    when {
        link.isNullOrBlank() -> Text(
            modifier = modifier,
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )

        else -> Text(
            modifier = modifier.clickable { uriHandler.openUri(uri = link) },
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}
