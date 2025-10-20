package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
internal actual fun Register(
    key: Any,
    onBackground: () -> Unit,
    onForeground: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableLifecycleCoroutine(lifecycleOwner, key) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> onBackground()
                Lifecycle.Event.ON_RESUME -> onForeground()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
