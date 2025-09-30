package com.hybris.tlv

import androidx.compose.ui.window.ComposeUIViewController
import com.hybris.tlv.telemetry.Telemetry

fun MainViewController() = ComposeUIViewController {
    Telemetry.init()
    App()
}
