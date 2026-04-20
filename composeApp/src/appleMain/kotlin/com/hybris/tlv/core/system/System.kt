package com.hybris.tlv.core.system

<<<<<<<< HEAD:composeApp/src/appleMain/kotlin/com/hybris/tlv/core/system/System.kt
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug: Boolean by lazy {
    Platform.isDebugBinary
}
========
internal actual val platform: Platform = Platform.Ios
>>>>>>>> 2f58d05d8c40c2f2e45ad43fc6e4355f76c55aed:composeApp/src/appleMain/kotlin/com/hybris/tlv/core/platform/Platform.kt
