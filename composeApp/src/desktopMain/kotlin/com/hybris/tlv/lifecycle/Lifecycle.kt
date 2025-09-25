package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hybris.tlv.LocalWindowState

@Composable
internal actual fun Register(
    key: Any,
    onPause: () -> Unit,
    onResume: () -> Unit,
) {
    val lifecycleOwner = LocalWindowState.current
    LaunchedEffect(keys = arrayOf(lifecycleOwner?.isMinimized)) {
        when {
            lifecycleOwner?.isMinimized == true -> onPause()
            else -> onResume()
        }
    }
}
