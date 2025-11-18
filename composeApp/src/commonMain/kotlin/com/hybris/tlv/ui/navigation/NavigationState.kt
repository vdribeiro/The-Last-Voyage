package com.hybris.tlv.ui.navigation

internal data class NavigationState(
    val route: Route = Route.Splash,
    val stateBuilder: Any? = null
)
