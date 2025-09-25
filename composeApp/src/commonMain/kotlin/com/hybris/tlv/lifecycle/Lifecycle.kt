package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable

@Composable
internal expect fun register(
    onPause: () -> Unit = {},
    onResume: () -> Unit = {},
    onDestroy: () -> Unit = {}
)
