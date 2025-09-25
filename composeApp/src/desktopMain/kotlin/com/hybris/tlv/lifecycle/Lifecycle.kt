package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.hybris.tlv.LocalWindow

@Composable
internal actual fun Register(
    key: Any,
    onPause: () -> Unit,
    onResume: () -> Unit,
) {
    val lifecycleOwner = LocalWindow.current
    DisposableEffect(keys = arrayOf(lifecycleOwner.isActive, key)) {
        if (!lifecycleOwner.isActive) onPause() else onResume()

        onDispose {}
    }
}
