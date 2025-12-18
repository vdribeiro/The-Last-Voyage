package com.hybris.tlv.cheats

import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Command
import com.hybris.tlv.ui.screen.sendCommand
import com.hybris.tlv.ui.theme.modifier.Gesture
import com.hybris.tlv.ui.theme.modifier.onGesture
import com.hybris.tlv.ui.theme.modifier.rememberKeySequence

/**
 * [Key] sequence that represent the Konami code.
 */
internal val konamiCode = listOf(
    Key.DirectionUp, Key.DirectionUp, Key.DirectionDown, Key.DirectionDown,
    Key.DirectionLeft, Key.DirectionRight, Key.DirectionLeft, Key.DirectionRight,
    Key.B, Key.A, Key.Enter
)

/**
 * [Gesture] sequence that represent a gesture-based version of the Konami code.
 */
internal val konamiGestureCode = listOf(
    Gesture.SWIPE_UP, Gesture.SWIPE_UP, Gesture.SWIPE_DOWN, Gesture.SWIPE_DOWN,
    Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT, Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT,
    Gesture.TAP, Gesture.TAP, Gesture.TAP
)

/**
 * Composable function that remembers the [konamiCode] and triggers a navigation to the cheat screen.
 */
@Composable
internal fun rememberKeySequenceCheats(): (KeyEvent) -> Boolean {
    val scope = rememberCoroutineScope()
    return rememberKeySequence(sequence = konamiCode) {
        scope.launch { sendCommand(command = Command.Navigate(screen = Screen.Cheat)) }
    }
}

/**
 * [Modifier] that enables [konamiGestureCode] detection and triggers a navigation to the cheat screen.
 */
internal fun Modifier.enableGestureCheats(): Modifier = composed {
    val scope = rememberCoroutineScope()
    onGesture(sequence = konamiGestureCode) {
        scope.launch { sendCommand(command = Command.Navigate(screen = Screen.Cheat)) }
    }
}
