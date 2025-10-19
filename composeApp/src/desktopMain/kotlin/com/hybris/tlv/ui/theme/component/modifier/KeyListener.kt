package com.hybris.tlv.ui.theme.component.modifier

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isBackPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent

/**
 * Registers a back navigation handler.
 */
@OptIn(ExperimentalComposeUiApi::class)
internal fun Modifier.registerBackNavigation(onBackNavigation: () -> Unit): Modifier =
    onPointerEvent(eventType = PointerEventType.Press) {
        with(receiver = it.buttons) {
            if (isBackPressed || isSecondaryPressed) onBackNavigation()
        }
    }
