package com.hybris.tlv.ui.screen.splash

internal sealed interface SplashAction {
    data object Feedback: SplashAction
    data object Next: SplashAction
}

internal data class SplashState(
    val loading: Boolean = true,
    val progress: Float = 0f,
    val currentContent: Content = Content.SPLASH,
    val showFeedback: Boolean = false
)

internal enum class Content {
    SPLASH,
    INTRO
}
