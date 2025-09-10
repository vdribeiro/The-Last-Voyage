package com.hybris.tlv.ui.screen.splash

internal sealed interface SplashAction {
    data object Start: SplashAction
}

internal data class SplashState(
    val progress: Float? = null,
)
