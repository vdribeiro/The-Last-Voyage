package com.hybris.tlv.ui.cheats

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.navigate
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
internal fun rememberKeySequenceCheats(navController: NavHostController): (KeyEvent) -> Boolean =
    rememberKeySequence(sequence = konamiCode) { navController.navigate(screen = Screen.Cheat) }

/**
 * [Modifier] that enables [konamiGestureCode] detection and triggers a navigation to the cheat screen.
 */
internal fun Modifier.enableGestureCheats(navController: NavHostController): Modifier =
    onGesture(sequence = konamiGestureCode) { navController.navigate(screen = Screen.Cheat) }
