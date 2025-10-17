package com.hybris.tlv.ui.theme.component.modifier

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
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

/**
 * Processes a [KeyEvent] to check its [progress] against a given key [sequence]
 * and return the new progress value after processing the event.
 */
internal fun KeyEvent.onSequence(
    sequence: List<Key>,
    progress: Int,
    onSequenceComplete: () -> Unit
): Int {
    if (type != KeyEventType.KeyDown) return progress
    val expectedKey = sequence.getOrNull(index = progress)
    val newProgress = when (key) {
        expectedKey -> progress + 1
        else -> if (key == sequence.firstOrNull()) 1 else 0
    }
    if (newProgress >= sequence.size) {
        onSequenceComplete()
        return 0
    }
    return newProgress
}