package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App(navigation: NavigationManager) {
    AppTheme {
        BackHandler(enabled = true) { navigation.back() }

        val navigationState by navigation.stateFlow.collectAsState()
        navigation.Screen(state = navigationState)
    }
}

internal val core: Core by lazy {
    Core()
}
