package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.hybris.tlv.LocalWindowState

@Composable
internal actual fun Register(
    key: Any,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onDestroy: () -> Unit
) {
    val windowState = LocalWindowState.current

    LaunchedEffect(key1 = windowState?.isMinimized) {
        when {
            windowState?.isMinimized == true -> onPause()
            else -> onResume()
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            onDestroy()
        }
    }
}
