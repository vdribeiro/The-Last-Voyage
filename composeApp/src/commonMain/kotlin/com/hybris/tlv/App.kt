package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.AudioPlayer

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App() = AppTheme {
    val navigation = core.navigation
    if (navigation == null) return@AppTheme

    BackHandler(enabled = true) { navigation.back() }
    val navigationState by navigation.stateFlow.collectAsState()
    navigation.Screen(state = navigationState)

    AudioPlayer(screen = navigationState.screen)
}

internal val core: Core by lazy {
    Core()
}
