package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable

@Composable
internal expect fun Register(
    key: Any = Unit,
    onPause: () -> Unit = {},
    onResume: () -> Unit = {},
    onDestroy: () -> Unit = {}
)
