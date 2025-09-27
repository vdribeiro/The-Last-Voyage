package com.hybris.tlv.ui.screen.splash

internal sealed interface SplashAction

internal sealed interface SplashStateBuilder {
    data object Default: SplashStateBuilder
}

internal data class SplashState(
    val progress: Float = 0f
)
