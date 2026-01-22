@file:ExcludeFromTesting

package com.hybris.tlv

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import com.hybris.tlv.TLV.App
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ExcludeFromTesting

private const val TAG = "App"

@OptIn(ExperimentalComposeUiApi::class)
fun main() = ComposeViewport {
    Telemetry.init()
    Telemetry.info(tag = TAG, message = "App started")

    App(modifier = Modifier)
}
