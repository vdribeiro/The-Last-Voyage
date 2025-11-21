@file:Suppress("unused", "FunctionName")

package com.hybris.tlv

import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.hybris.tlv.telemetry.Telemetry

private const val TAG = "App"
private val dependency: Dependency by lazy { Dependency() }

fun MainViewController() = ComposeUIViewController {
    Telemetry.init()
    Telemetry.info(tag = TAG, message = "App started")

    App(
        modifier = Modifier.enableGestureCheats(),
        config = dependency.config,
        useCases = dependency.useCases,
        audioPlayer = dependency.audioPlayer,
    )
}
