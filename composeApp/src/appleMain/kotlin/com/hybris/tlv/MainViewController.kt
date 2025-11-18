@file:Suppress("unused", "FunctionName")

package com.hybris.tlv

import androidx.compose.ui.window.ComposeUIViewController
import com.hybris.tlv.telemetry.Telemetry

private const val TAG = "APP"

fun MainViewController() = ComposeUIViewController {
    Telemetry.init()
    Telemetry.info(tag = TAG, message = "App started")

    App(dependency = dependency)
}
