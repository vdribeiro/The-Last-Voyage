package com.hybris.tlv.ui.navigation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.composed

@OptIn(ExperimentalComposeUiApi::class)
internal actual fun Modifier.backNavigation(onBack: () -> Unit): Modifier = composed {
    BackHandler(enabled = true, onBack = onBack)
    this
}
