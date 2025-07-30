package com.hybris.tlv.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hybris.tlv.usecase.credits.model.Credits

@Composable
internal fun CreditsText(uriHandler: UriHandler, credits: Credits) {
    Spacer(modifier = Modifier.height(height = 8.dp))
    when {
        credits.link.isNullOrBlank() -> Text(
            text = credits.id,
            style = MaterialTheme.typography.bodyLarge
        )

        else -> Text(
            modifier = Modifier.clickable { uriHandler.openUri(uri = credits.link) },
            text = credits.id,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}
