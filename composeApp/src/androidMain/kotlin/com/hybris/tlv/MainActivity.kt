package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.component.modifier.onGesture

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Telemetry.init()
        enableEdgeToEdge()
        setContent { App(modifier = Modifier.onGesture(sequence = konamiGestureCode) { setKonamiCode() }) }
    }
}
