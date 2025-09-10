package com.hybris.tlv.ui.screen.splash

internal data class SplashState(
    val progress: Float = 0f,
)

internal sealed interface SplashAction {
    data object Start: SplashAction
}
