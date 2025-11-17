package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hybris.tlv.telemetry.Telemetry

private val dependency: Dependency by lazy { Dependency() }

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Telemetry.init()
        enableEdgeToEdge()
        setContent { App(dependency = dependency) }
    }
}
