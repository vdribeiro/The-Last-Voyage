package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.hybris.tlv.LocalWindowState

@Composable
internal actual fun Register(
    key: Any,
    onPause: () -> Unit,
    onResume: () -> Unit,
) {
    val lifecycleOwner = LocalWindowState.current
    DisposableEffect(keys = arrayOf(lifecycleOwner.isMinimized, key)) {
        when {
            lifecycleOwner.isMinimized -> onPause()
            else -> onResume()
        }

        onDispose {}
    }
}
