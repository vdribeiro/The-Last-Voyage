package com.hybris.tlv.ui.theme.component.text

import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography

@Composable
internal fun TypewriterText(
    modifier: Modifier = Modifier,
    text: String? = null,
    delay: Long = 50L
) {
    val isPreview = LocalInspectionMode.current
    val typography = LocalTypography.current

    val words = remember(key1 = text) { text?.split(' ').orEmpty() }
    var visibleWordsCount by remember(key1 = text) { mutableStateOf(value = 0) }
    var isRevealed by remember(key1 = text) { mutableStateOf(value = isPreview) }
    if (!isPreview) LaunchedEffect(key1 = text) {
        visibleWordsCount = 0
        isRevealed = false
        while (visibleWordsCount < words.size && !isRevealed) {
            delay(timeMillis = delay)
            visibleWordsCount++
        }
        if (!isRevealed) isRevealed = true
    }
    Box(
        modifier = modifier.clickable { isRevealed = true },
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState()),
            text = if (isRevealed) text else words.take(n = visibleWordsCount).joinToString(separator = " "),
            style = typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun Preview() = AppTheme {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        TypewriterText(text = "Text")
        TypewriterText()
    }
}
