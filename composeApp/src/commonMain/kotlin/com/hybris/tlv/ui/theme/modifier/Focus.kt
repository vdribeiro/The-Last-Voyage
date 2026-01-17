package com.hybris.tlv.ui.theme.modifier

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager

/**
 * Clear focus when this composable is tapped.
 */
internal fun Modifier.clearFocus(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    pointerInput(key1 = Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
}
