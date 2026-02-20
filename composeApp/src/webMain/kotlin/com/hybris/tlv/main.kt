@file:ExcludeFromTesting

package com.hybris.tlv

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.TLV.App
import com.hybris.tlv.test.ExcludeFromTesting

@OptIn(ExperimentalComposeUiApi::class)
fun main() = ComposeViewport {
    val navController = rememberNavController()
    App(
        modifier = Modifier,
        navController = navController
    )
}
