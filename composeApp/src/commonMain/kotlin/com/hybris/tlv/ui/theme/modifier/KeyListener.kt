package com.hybris.tlv.ui.theme.modifier

import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

/**
 * A [Modifier] that listens for a specific [sequence] of [Key] stokes and triggers the [onSequenceComplete] callback upon completion.
 * The sequence progress will automatically reset if the user pauses for longer than the specified [delay].
 */
internal fun Modifier.onKeySequence(
    sequence: List<Key>,
    delay: Long = 1500L,
    onSequenceComplete: () -> Unit
): Modifier = composed {
    var progress by remember { mutableStateOf(value = 0) }
    LaunchedEffect(key1 = progress) {
        if (progress > 0) {
            delay(timeMillis = delay)
            progress = 0
        }
    }
    onPreviewKeyEvent(onPreviewKeyEvent = remember(key1 = sequence, key2 = onSequenceComplete) {
        { keyEvent ->
            progress = keyEvent.onSequence(
                sequence = sequence,
                progress = progress,
                onSequenceComplete = onSequenceComplete
            )
            false
        }
    })
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
