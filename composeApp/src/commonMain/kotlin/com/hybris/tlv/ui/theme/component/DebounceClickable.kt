package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal fun Modifier.debouncedClickable(
    enabled: Boolean = true,
    debounceTime: Duration = 500.milliseconds,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(value = Instant.DISTANT_PAST) }
    clickable(enabled = enabled) {
        val now = Clock.System.now()
        val elapsed = now - lastClickTime
        if (elapsed > debounceTime) {
            lastClickTime = now
            onClick()
        }
    }
}
