package com.hybris.tlv

import androidx.compose.ui.input.key.Key
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

internal fun setKonamiCode() {
    Telemetry.feedback(message = "Konami Code!")
    dependency.dispatcher.default.launch {
        dependency.config.setPreferences { it.copy(cheats = true) }
    }
}
