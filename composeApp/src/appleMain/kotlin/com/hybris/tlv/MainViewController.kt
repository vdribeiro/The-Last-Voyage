@file:Suppress("unused", "FunctionName")
@file:ExcludeFromTesting

package com.hybris.tlv

import platform.UIKit.UIViewController
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.hybris.tlv.TLV.App
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.cheats.enableGestureCheats

fun MainViewController(): UIViewController = ComposeUIViewController {
    App(modifier = Modifier.enableGestureCheats())
}
