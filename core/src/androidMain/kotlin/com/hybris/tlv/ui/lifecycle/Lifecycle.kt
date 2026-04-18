package com.hybris.tlv.ui.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
actual fun Register(
    key: Any,
    onBackground: () -> Unit,
    onForeground: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, key2 = key) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> onBackground()
                Lifecycle.Event.ON_RESUME -> onForeground()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer = observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer = observer)
        }
    }
}
