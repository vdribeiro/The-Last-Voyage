package com.hybris.tlv.lifecycle

import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalInspectionMode

/**
 * A wrapper around [LaunchedEffect] that only runs when the composable is not in inspection mode.
 */
@Composable
internal fun LifecycleCoroutine(vararg keys: Any?, block: suspend CoroutineScope.() -> Unit) {
    if (LocalInspectionMode.current) return
    LaunchedEffect(*keys, block = block)
}

/**
 * A wrapper around [DisposableEffect] that only runs when the composable is not in inspection mode.
 */
@Composable
internal fun DisposableLifecycleCoroutine(vararg keys: Any?, block: DisposableEffectScope.() -> DisposableEffectResult) {
    if (LocalInspectionMode.current) return
    DisposableEffect(*keys, effect = block)
}
