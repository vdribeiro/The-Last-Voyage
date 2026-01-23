package com.hybris.tlv.domain.flag

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import com.hybris.tlv.infrastructure.platform.Platform
import com.hybris.tlv.infrastructure.platform.isDebug
import com.hybris.tlv.infrastructure.platform.platform

internal object FeatureFlags {
    private val _flags: MutableStateFlow<Flags> = MutableStateFlow(
        value = Flags(
            devMode = isDebug,
            reset = platform == Platform.Web,
            http = true,
            networkQuality = true,
            archive = false,
            music = true
        )
    )
    val flags: Flags get() = _flags.value

    /**
     * Sets feature flags.
     */
    fun set(flags: (Flags) -> Flags): FeatureFlags = apply {
        _flags.update(function = flags)
    }
}
