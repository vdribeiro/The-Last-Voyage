package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import com.hybris.tlv.flow.launch
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.component.modifier.Gesture

internal val konamiCode = listOf(
    Key.DirectionUp, Key.DirectionUp, Key.DirectionDown, Key.DirectionDown,
    Key.DirectionLeft, Key.DirectionRight, Key.DirectionLeft, Key.DirectionRight,
    Key.B, Key.A, Key.Enter
)

internal val konamiGestureCode = listOf(
    Gesture.SWIPE_UP, Gesture.SWIPE_UP, Gesture.SWIPE_DOWN, Gesture.SWIPE_DOWN,
    Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT, Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT,
    Gesture.TAP, Gesture.TAP, Gesture.TAP
)

@Composable
internal fun rememberKeySequence(
    sequence: List<Key>,
    onSequenceComplete: () -> Unit
): (KeyEvent) -> Boolean {
    var progress by remember { mutableStateOf(value = 0) }
    return remember {
        { keyEvent ->
            progress = keyEvent.onSequence(
                sequence = sequence,
                progress = progress,
                onSequenceComplete = onSequenceComplete
            )
            false
        }
    }
}

/**
 * Processes a [KeyEvent] to check its [progress] against a given key [sequence]
 * and return the new progress value after processing the event.
 */
private fun KeyEvent.onSequence(
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

internal fun setKonamiCode() {
    Telemetry.feedback(message = "Konami Code!")
    dependency.dispatcher.default.launch {
        dependency.config.setPreferences { it.copy(cheats = true) }
    }
}
