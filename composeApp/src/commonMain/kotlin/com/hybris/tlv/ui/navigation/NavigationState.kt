package com.hybris.tlv.ui.navigation

internal data class NavigationState(
    val screen: Screen = Screen.Splash,
    val stateBuilder: Any? = null
)
