package com.hybris.tlv.ui.theme.component.modifier

import kotlin.math.abs
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Processes [Gesture]s and check if it matches a given [sequence].
 * If so, it executes [onSequenceComplete] callback.
 */
internal fun Modifier.onGesture(
    sequence: List<Gesture>,
    delay: Long = 2000L,
    onSequenceComplete: () -> Unit
): Modifier = composed {
    val nestedScrollConnection = remember {
        object: NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }
        }
    }

    val progress = remember { mutableStateListOf<Gesture>() }
    LaunchedEffect(key1 = progress.size) {
        if (progress.isNotEmpty()) {
            delay(timeMillis = delay)
            if (progress.size < sequence.size) progress.clear()
        }
    }
    fun checkSequence(gesture: Gesture) {
        progress.add(element = gesture)
        if (progress.takeLast(n = sequence.size) == sequence) {
            onSequenceComplete()
            progress.clear()
        }
    }


    this
        .nestedScroll(connection = nestedScrollConnection)
        .pointerInput(key1 = Unit) {
            coroutineScope {
                launch {
                    detectTapGestures {
                        checkSequence(gesture = Gesture.TAP)
                    }
                }
                launch {
                    var dragStart: Offset = Offset.Zero
                    var totalDrag: Offset = Offset.Zero
                    detectDragGestures(
                        onDragStart = { dragStart = it },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            totalDrag = change.position - dragStart
                        },
                        onDragEnd = {
                            val gesture = onDragGestures(offset = totalDrag) ?: return@detectDragGestures
                            if (progress.lastOrNull() != gesture) checkSequence(gesture = gesture)
                            dragStart = Offset.Zero
                            totalDrag = Offset.Zero
                        },
                    )
                }
            }
        }
}

private fun onDragGestures(offset: Offset): Gesture? {
    //if (offset.getDistance() < 100) return null
    val vertical = abs(x = offset.y)
    val horizontal = abs(x = offset.x)
    val leniency = 2.0
    return when {
        vertical > horizontal * leniency -> if (offset.y > 0) Gesture.SWIPE_DOWN else Gesture.SWIPE_UP
        horizontal > vertical * leniency -> if (offset.x > 0) Gesture.SWIPE_RIGHT else Gesture.SWIPE_LEFT
        else -> null
    }
}

internal enum class Gesture {
    SWIPE_UP,
    SWIPE_DOWN,
    SWIPE_LEFT,
    SWIPE_RIGHT,
    TAP
}
