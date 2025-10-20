package com.hybris.tlv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun Modifier.backNavigation(onBack: () -> Unit): Modifier {
    BackHandler(enabled = true, onBack = onBack)
    return this
}