package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.launch
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.modifier.Gesture
import com.hybris.tlv.ui.theme.modifier.onGesture
import com.hybris.tlv.ui.theme.modifier.rememberKeySequence

private val konamiCode = listOf(
    Key.DirectionUp, Key.DirectionUp, Key.DirectionDown, Key.DirectionDown,
    Key.DirectionLeft, Key.DirectionRight, Key.DirectionLeft, Key.DirectionRight,
    Key.B, Key.A, Key.Enter
)

private val konamiGestureCode = listOf(
    Gesture.SWIPE_UP, Gesture.SWIPE_UP, Gesture.SWIPE_DOWN, Gesture.SWIPE_DOWN,
    Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT, Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT,
    Gesture.TAP, Gesture.TAP, Gesture.TAP
)

private fun setKonamiCode(config: ConfigManager) =
    Dispatcher.Default.launch {
        Telemetry.feedback(message = "Konami Code!")
        config.setPreferences { it.copy(cheats = !it.cheats) }.savePreferences()
    }

@Composable
internal fun rememberCheats(config: ConfigManager): (KeyEvent) -> Boolean =
    rememberKeySequence(sequence = konamiCode) { setKonamiCode(config = config) }

internal fun Modifier.enableCheats(config: ConfigManager): Modifier =
    onGesture(sequence = konamiGestureCode) { setKonamiCode(config = config) }
