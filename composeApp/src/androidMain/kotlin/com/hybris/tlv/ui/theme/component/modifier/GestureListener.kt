package com.hybris.tlv.ui.theme.component.modifier

import kotlin.math.abs
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

@Composable
internal fun Modifier.onGesture(
    sequence: List<Gesture>,
    onSequenceComplete: () -> Unit
): Modifier = composed {
    val progress = remember { mutableStateListOf<Gesture>() }
    LaunchedEffect(key1 = progress.size) {
        if (progress.isNotEmpty()) {
            delay(timeMillis = 1500L)
            if (progress.size < sequence.size) {
                progress.clear()
            }
        }
    }
    fun checkSequence(gesture: Gesture) {
        progress.add(element = gesture)
        if (progress.takeLast(n = sequence.size) == sequence) {
            onSequenceComplete()
            progress.clear()
        }
    }

    pointerInput(key1 = Unit) {
        coroutineScope {
            launch {
                detectTapGestures {
                    checkSequence(gesture = Gesture.TAP)
                }
            }
            launch {
                detectDragGestures { change, offset ->
                    change.consume()
                    val gesture = onDragGestures(offset = offset)
                    if (progress.lastOrNull() != gesture) checkSequence(gesture = gesture)
                }
            }
        }
    }
}

private fun onDragGestures(offset: Offset): Gesture =
    when {
        abs(x = offset.x) > abs(x = offset.y) && offset.x > 0 -> Gesture.SWIPE_RIGHT
        abs(x = offset.x) > abs(x = offset.y) && offset.x < 0 -> Gesture.SWIPE_LEFT
        abs(x = offset.y) > abs(x = offset.x) && offset.y > 0 -> Gesture.SWIPE_DOWN
        else -> Gesture.SWIPE_UP
    }

internal enum class Gesture {
    SWIPE_UP,
    SWIPE_DOWN,
    SWIPE_LEFT,
    SWIPE_RIGHT,
    TAP
}
