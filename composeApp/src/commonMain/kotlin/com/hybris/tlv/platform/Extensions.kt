package com.hybris.tlv.platform

internal val isAndroid by lazy { getPlatform() == Platform.Android }

// Some greedy companies do not allow Kofi banner...
internal val isIos by lazy { getPlatform() == Platform.Ios }

internal val isDesktop by lazy {
    val platform = getPlatform()
    platform == Platform.Windows || platform == Platform.Mac || platform == Platform.Linux
}
