package com.hybris.tlv.ui.theme.modifier

import kotlin.math.abs
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hybris.tlv.lifecycle.LifecycleCoroutine

/**
 * A [Modifier] that listens for a specific [sequence] of [Gesture]s and triggers the [onSequenceComplete] callback upon completion.
 * This modifier acts as a global gesture detector, capable of capturing both taps and swipes even when layered over scrollable components.
 * The gesture progress will automatically reset if the user pauses for longer than the specified [delay].
 * The gesture can be configured with a minimum distance [thresholdDp] it must cover and a [leniency] factor to allow for imperfectly straight swipes (1.0 is perfectly straight).
 */
internal fun Modifier.onGesture(
    sequence: List<Gesture>,
    delay: Long = 1000L,
    thresholdDp: Dp = 60.dp,
    leniency: Double = 2.0,
    onSequenceComplete: () -> Unit
): Modifier = composed {
    val progress = remember { mutableStateListOf<Gesture>() }
    LifecycleCoroutine(progress.size) {
        if (progress.isNotEmpty()) {
            delay(timeMillis = delay)
            progress.clear()
        }
    }

    fun checkSequence(gesture: Gesture?) {
        if (gesture == null) return
        progress.add(element = gesture)
        if (progress.takeLast(n = sequence.size) == sequence) {
            onSequenceComplete()
            progress.clear()
        }
    }

    val threshold: Float = with(receiver = LocalDensity.current) { thresholdDp.toPx() }
    var gestureDragTotal by remember { mutableStateOf(value = Offset.Zero) }
    LifecycleCoroutine(gestureDragTotal) {
        if (gestureDragTotal != Offset.Zero) {
            delay(timeMillis = 100)
            checkSequence(
                gesture = onDragGestures(
                    offset = gestureDragTotal,
                    threshold = threshold,
                    leniency = leniency
                )
            )
            gestureDragTotal = Offset.Zero
        }
    }

    val nestedScrollConnection = remember {
        object: NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput) gestureDragTotal += available
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source == NestedScrollSource.UserInput) gestureDragTotal += consumed
                return super.onPostScroll(consumed, available, source)
            }
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
                    awaitEachGesture {
                        val down: PointerInputChange = awaitFirstDown(requireUnconsumed = false)
                        var totalDrag = Offset.Zero
                        val slopPassed = awaitTouchSlopOrCancellation(pointerId = down.id) { change, overSlop ->
                            change.consume()
                            totalDrag = overSlop
                        }
                        if (slopPassed == null) return@awaitEachGesture
                        drag(pointerId = slopPassed.id) { change ->
                            totalDrag += change.position - change.previousPosition
                            change.consume()
                        }
                        checkSequence(
                            gesture = onDragGestures(
                                offset = totalDrag,
                                threshold = threshold,
                                leniency = leniency
                            )
                        )
                    }
                }
            }
        }
}

/**
 * Determines if a drag [offset], with a minimum distance [threshold] in pixels it must cover, constitutes a directional swipe.
 * The offset should be the total vector of the drag gesture, from start to end.
 * A [leniency] factor is used to allow for imperfectly straight swipes.
 */
private fun onDragGestures(
    offset: Offset,
    threshold: Float,
    leniency: Double
): Gesture? {
    if (offset.getDistance() < threshold) return null
    val vertical = abs(x = offset.y)
    val horizontal = abs(x = offset.x)
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
