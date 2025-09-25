package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.delay

/**
 * A LinearProgressIndicator that only appears after a certain delay.
 */
@Composable
fun Loading(
    modifier: Modifier = Modifier,
    delayMillis: Long = 300L,
    progress: (() -> Float)? = null,
) {
    val inspection = LocalInspectionMode.current
    var show by remember { mutableStateOf(value = inspection) }
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = delayMillis)
        show = true
    }
    val callback = if (inspection && progress == null) {
        { 0.5f }
    } else progress
    if (show) when (callback) {
        null -> LinearProgressIndicator(modifier = modifier.fillMaxWidth())
        else -> LinearProgressIndicator(modifier = modifier.fillMaxWidth(), progress = callback)
    }
}
