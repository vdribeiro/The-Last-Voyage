package com.hybris.tlv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
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

private fun setKonamiCode(scope: CoroutineScope, config: ConfigManager): Job =
    scope.launch(context = Dispatcher.IO) {
        Telemetry.feedback(message = "Konami Code!")
        config.setPreferences { it.copy(cheats = !it.cheats) }
    }

@Composable
internal fun rememberKeySequenceCheats(config: ConfigManager): (KeyEvent) -> Boolean {
    val scope = rememberCoroutineScope()
    return rememberKeySequence(sequence = konamiCode) { setKonamiCode(scope = scope, config = config) }
}

internal fun Modifier.enableGestureCheats(config: ConfigManager): Modifier = composed {
    val scope = rememberCoroutineScope()
    onGesture(sequence = konamiGestureCode) { setKonamiCode(scope = scope, config = config) }
}
