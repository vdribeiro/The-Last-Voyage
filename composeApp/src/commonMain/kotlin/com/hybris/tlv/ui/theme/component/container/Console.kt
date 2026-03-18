package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun Console(
    modifier: Modifier = Modifier,
    logs: String? = null
) {
    val consoleTranslation = getTranslation(key = "error_screen__console")

    val typography = LocalTypography.current

    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = logs) { scrollState.scrollTo(value = scrollState.maxValue) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        Text(
            text = consoleTranslation,
            style = typography.labelLarge
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = scrollState),
            text = logs,
            style = typography.labelSmall
        )
    }
}

@Preview
@Composable
private fun ConsolePreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "error_screen__console",
                value = "Stacktrace"
            ),
        )
    )
    Console(logs = "Some very interesting logs")
}
