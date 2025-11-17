@file:Suppress("unused", "FunctionName")

package com.hybris.tlv

import androidx.compose.ui.window.ComposeUIViewController
import com.hybris.tlv.telemetry.Telemetry

private val dependency: Dependency by lazy { Dependency() }

fun MainViewController() = ComposeUIViewController {
    Telemetry.init()
    App(dependency = dependency)
}
