@file:ExcludeFromTesting

package com.hybris.tlv

import platform.UIKit.UIViewController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.TLV.dependency
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.App
import com.hybris.tlv.ui.cheats.enableGestureCheats

fun MainViewController(): UIViewController = ComposeUIViewController {
    val navController = rememberNavController()
    val dependency by dependency.collectAsState()
    App(
        modifier = Modifier.enableGestureCheats(navController = navController),
        navController = navController,
        dependency = dependency
    )
}
