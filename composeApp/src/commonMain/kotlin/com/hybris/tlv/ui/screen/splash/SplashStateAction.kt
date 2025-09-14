package com.hybris.tlv.ui.screen.splash

internal sealed interface SplashAction

internal data class SplashState(
    val progress: Float,
)
