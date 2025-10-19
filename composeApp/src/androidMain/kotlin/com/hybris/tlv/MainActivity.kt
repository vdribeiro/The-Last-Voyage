package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.hybris.tlv.flow.launch
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.component.modifier.Gesture
import com.hybris.tlv.ui.theme.component.modifier.onGesture

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Telemetry.init()
        enableEdgeToEdge()
        setContent {
            App(modifier = Modifier.onGesture(sequence = konamiGestureCode) {
                Telemetry.feedback(message = "Konami Code!")
                dependency.dispatcher.default.launch {
                    dependency.config.setPreferences { it.copy(cheats = true) }
                }
            })
        }
    }
}

private val konamiGestureCode = listOf(
    Gesture.SWIPE_UP, Gesture.SWIPE_UP, Gesture.SWIPE_DOWN, Gesture.SWIPE_DOWN,
    Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT, Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT,
    Gesture.TAP, Gesture.TAP, Gesture.TAP
)
