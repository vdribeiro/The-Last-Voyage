package com.hybris.tlv.platform

internal sealed interface Platform {
    object Android: Platform
    object Ios: Platform
    object Windows: Platform
    object Mac: Platform
    object Linux: Platform
    object Unknown: Platform
}

internal expect val isDebug: Boolean

internal expect fun getPlatform(): Platform

internal val isMobile by lazy {
    val platform = getPlatform()
    platform == Platform.Android || platform == Platform.Ios
}

internal val isDesktop by lazy {
    val platform = getPlatform()
    platform == Platform.Windows || platform == Platform.Mac || platform == Platform.Linux
}

// Some greedy companies do not allow Kofi banner...
internal val isSupercilious by lazy { getPlatform() == Platform.Ios }
