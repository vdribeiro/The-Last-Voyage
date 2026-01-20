@file:ExcludeFromTesting

package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.hybris.tlv.TLV.App
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.cheats.enableGestureCheats

private const val TAG = "App"

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Telemetry.init()
        Telemetry.info(tag = TAG, message = "App started")

        enableEdgeToEdge()
        setContent {
            App(modifier = Modifier.enableGestureCheats())
        }
    }
}
