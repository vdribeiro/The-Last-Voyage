@file:ExcludeFromTesting

package com.hybris.tlv

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeViewport
import com.hybris.tlv.Application.dependency
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.App

fun main() = ComposeViewport {
    val dependency by dependency.collectAsState()
    App(dependency = dependency)
}
