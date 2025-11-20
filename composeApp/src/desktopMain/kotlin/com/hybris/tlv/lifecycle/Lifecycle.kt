package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.hybris.tlv.LocalWindowState

@Composable
internal actual fun Register(
    key: Any,
    onBackground: () -> Unit,
    onForeground: () -> Unit,
) {
    val lifecycleOwner = LocalWindowState.current
    DisposableEffect(key1 = lifecycleOwner.isMinimized, key2 = key) {
        if (lifecycleOwner.isMinimized) onBackground() else onForeground()

        onDispose {}
    }
}
