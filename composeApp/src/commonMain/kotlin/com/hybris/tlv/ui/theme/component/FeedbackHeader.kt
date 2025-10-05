package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun FeedbackHeader(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {
    val typography = LocalTypography.current

    Box(modifier = modifier) {
        Icon(
            modifier = Modifier.size(size = 64.dp),
            imageVector = Icons.Outlined.BugReport,
            contentDescription = "Feedback Icon",
        )
        Text(
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 8.dp
            ),
            text = title,
            style = typography.headlineSmall
        )
        Text(
            text = description,
            style = typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}
