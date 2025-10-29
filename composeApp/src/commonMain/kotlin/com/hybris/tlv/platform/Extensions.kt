package com.hybris.tlv.platform

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
