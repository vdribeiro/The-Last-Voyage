package com.hybris.tlv.ui.theme.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.hybris.tlv.locale.nowEpoch

internal fun Modifier.debouncedClickable(
    debounceTime: Long = 1000L,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(value = 0L) }
    clickable {
        val now = nowEpoch()
        if (now - lastClickTime > debounceTime) {
            lastClickTime = now
            onClick()
        }
    }
}