package com.hybris.tlv

import androidx.compose.ui.window.ComposeUIViewController
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases

private val useCases: UseCases by lazy {
    Gateways()
}

private val navigation: NavigationManager by lazy {
    Navigation(useCases = useCases)
}

fun MainViewController() = ComposeUIViewController {
    App(navigation = navigation)
}
