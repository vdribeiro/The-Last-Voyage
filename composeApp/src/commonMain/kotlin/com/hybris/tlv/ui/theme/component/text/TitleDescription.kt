package com.hybris.tlv.ui.theme.component.text

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun TitleDescription(
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
) {
    val typography = LocalTypography.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 32.dp),
    ) {
        title?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                style = typography.titleLarge,
                text = it,
                textAlign = TextAlign.Center,
            )
        }
        description?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(state = rememberScrollState())
                    .padding(all = 8.dp),
                style = typography.bodyLarge,
                text = it,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Preview
@Composable
private fun TitleDescriptionPreview() = AppTheme {
    TitleDescription(
        title = "Title",
        description = "Description"
    )
}

@Preview
@Composable
private fun TitleDescriptionTitleOnlyPreview() = AppTheme {
    TitleDescription(title = "Title")
}

@Preview
@Composable
private fun TitleDescriptionDescriptionOnlyPreview() = AppTheme {
    TitleDescription(description = "Description")
}

@Preview
@Composable
private fun TitleDescriptionEmptyPreview() = AppTheme {
    TitleDescription()
}
