package com.hybris.tlv.ui.theme.component.text

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
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun TitleDescription(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
) {
    val typography = LocalTypography.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 32.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            style = typography.titleLarge,
            text = title,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
                .padding(all = 8.dp),
            style = typography.bodyLarge,
            text = description,
            textAlign = TextAlign.Start,
        )
    }
}
