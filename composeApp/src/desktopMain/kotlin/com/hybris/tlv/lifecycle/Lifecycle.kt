package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import com.hybris.tlv.LocalWindowState

@Composable
internal actual fun Register(
    key: Any,
    onBackground: () -> Unit,
    onForeground: () -> Unit,
) {
    val lifecycleOwner = LocalWindowState.current
    DisposableLifecycleCoroutine(lifecycleOwner.isMinimized, key) {
        if (lifecycleOwner.isMinimized) onBackground() else onForeground()

        onDispose {}
    }
}
