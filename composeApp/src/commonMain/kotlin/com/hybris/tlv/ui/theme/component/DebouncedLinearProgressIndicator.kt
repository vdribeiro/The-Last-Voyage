package com.hybris.tlv.ui.theme.component

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * A DebouncedLinearProgressIndicator that only appears after a certain delay.
 */
@Composable
fun DebouncedLinearProgressIndicator(
    modifier: Modifier = Modifier,
    delayMillis: Long = 300L,
    progress: (() -> Float)? = null,
) {
    var show by remember { mutableStateOf(value = false) }
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = delayMillis)
        show = true
    }
    if (show) when (progress) {
        null -> LinearProgressIndicator(modifier = modifier)
        else -> LinearProgressIndicator(modifier = modifier, progress = progress)
    }
}
