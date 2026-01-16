package com.hybris.tlv.cheats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import com.hybris.tlv.command.Command
import com.hybris.tlv.command.sendCommand
import com.hybris.tlv.ui.theme.modifier.onGesture

/**
 * [Key] sequence that represent the Konami code.
 */
internal val konamiCode = listOf(
    Key.DirectionUp, Key.DirectionUp, Key.DirectionDown, Key.DirectionDown,
    Key.DirectionLeft, Key.DirectionRight, Key.DirectionLeft, Key.DirectionRight,
    Key.B, Key.A, Key.Enter
)

/**
 * [com.hybris.tlv.ui.theme.modifier.Gesture] sequence that represent a gesture-based version of the Konami code.
 */
internal val konamiGestureCode = listOf(
    com.hybris.tlv.ui.theme.modifier.Gesture.SWIPE_UP,
    com.hybris.tlv.ui.theme.modifier.Gesture.SWIPE_UP,
    com.hybris.tlv.ui.theme.modifier.Gesture.SWIPE_DOWN,
    com.hybris.tlv.ui.theme.modifier.Gesture.SWIPE_DOWN,
    com.hybris.tlv.ui.theme.modifier.Gesture.SWIPE_LEFT,
    com.hybris.tlv.ui.theme.modifier.Gesture.SWIPE_RIGHT,
    com.hybris.tlv.ui.theme.modifier.Gesture.SWIPE_LEFT,
    com.hybris.tlv.ui.theme.modifier.Gesture.SWIPE_RIGHT,
    com.hybris.tlv.ui.theme.modifier.Gesture.TAP,
    com.hybris.tlv.ui.theme.modifier.Gesture.TAP,
    com.hybris.tlv.ui.theme.modifier.Gesture.TAP
)

/**
 * Composable function that remembers the [konamiCode] and triggers a navigation to the cheat screen.
 */
@Composable
internal fun rememberKeySequenceCheats(): (KeyEvent) -> Boolean =
    _root_ide_package_.com.hybris.tlv.ui.theme.modifier.rememberKeySequence(sequence = konamiCode) {
        sendCommand(command = Command.Navigate(screen = com.hybris.tlv.ui.navigation.Screen.Cheat))
    }

/**
 * [Modifier] that enables [konamiGestureCode] detection and triggers a navigation to the cheat screen.
 */
internal fun Modifier.enableGestureCheats(): Modifier = composed {
    onGesture(sequence = konamiGestureCode) {
        sendCommand(command = Command.Navigate(screen = com.hybris.tlv.ui.navigation.Screen.Cheat))
    }
}
