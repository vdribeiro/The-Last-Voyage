package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isBackPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun Modifier.backNavigation(onBack: () -> Unit): Modifier {
    BackHandler(enabled = true, onBack = onBack)
    return onPointerEvent(eventType = PointerEventType.Press) {
        with(receiver = it.buttons) {
            if (isBackPressed || isSecondaryPressed) onBack()
        }
    }
}
