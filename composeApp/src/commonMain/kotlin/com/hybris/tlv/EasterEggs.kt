package com.hybris.tlv

import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.LocalHapticFeedback
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.modifier.Gesture
import com.hybris.tlv.ui.theme.modifier.onGesture
import com.hybris.tlv.ui.theme.modifier.rememberKeySequence

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
internal fun rememberKeySequenceCheats(config: ConfigManager): (KeyEvent) -> Boolean {
    val scope = rememberCoroutineScope()
    return rememberKeySequence(sequence = konamiCode) {
        scope.launch(context = Dispatcher.IO) { enableCheats(config = config) }
    }
}

internal fun Modifier.enableGestureCheats(config: ConfigManager): Modifier = composed {
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    onGesture(sequence = konamiGestureCode) {
        haptics.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.Reject)
        scope.launch(context = Dispatcher.IO) { enableCheats(config = config) }
    }
}

private suspend fun enableCheats(config: ConfigManager) {
    config.setPreferences { it.copy(cheats = true) }
    Telemetry.info(tag = "God", message = "Cheats!")
}
