package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.hybris.tlv.telemetry.Telemetry

private const val TAG = "App"
private val dependency: Dependency by lazy { Dependency() }

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Telemetry.init()
        Telemetry.info(tag = TAG, message = "App started")

        enableEdgeToEdge()
        setContent {
            setContent {
                App(
                    modifier = Modifier.enableGestureCheats(config = dependency.config),
                    config = dependency.config,
                    useCases = dependency.useCases,
                    audioPlayer = dependency.audioPlayer,
                )
            }
        }
    }
}
