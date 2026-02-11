package com.hybris.tlv

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import com.hybris.tlv.TLV.App
import com.hybris.tlv.test.ExcludeFromTesting

@ExcludeFromTesting
@OptIn(ExperimentalComposeUiApi::class)
fun main() = ComposeViewport {
    App(modifier = Modifier)
}
