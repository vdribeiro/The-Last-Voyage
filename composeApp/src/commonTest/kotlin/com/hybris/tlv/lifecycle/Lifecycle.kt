package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
internal fun Register(
    key: Any = Unit,
    onBackground: () -> Unit = {},
    onForeground: () -> Unit = {},
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

internal val lifecycleOwner: LifecycleOwner
    get() = object: LifecycleOwner {
        override val lifecycle: LifecycleRegistry = LifecycleRegistry(provider = this)

        init {
            lifecycle.currentState = Lifecycle.State.RESUMED
        }
    }
