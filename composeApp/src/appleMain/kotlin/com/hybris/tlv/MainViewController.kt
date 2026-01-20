@file:Suppress("unused", "FunctionName")
@file:ExcludeFromTesting

package com.hybris.tlv

import platform.UIKit.UIViewController
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.hybris.tlv.TLV.App
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.cheats.enableGestureCheats

private const val TAG = "App"

fun MainViewController(): UIViewController = ComposeUIViewController {
    Telemetry.init()
    Telemetry.info(tag = TAG, message = "App started")

    App(modifier = Modifier.enableGestureCheats())
}
