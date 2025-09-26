package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable

/**
 * Registers a [onBackground] and [onForeground] callback for when the app goes into the background and foreground respectively.
 * The [key] is used to observe changes between recompositions and restart the callbacks observers.
 */
@Composable
internal expect fun Register(
    key: Any = Unit,
    onBackground: () -> Unit = {},
    onForeground: () -> Unit = {},
)
