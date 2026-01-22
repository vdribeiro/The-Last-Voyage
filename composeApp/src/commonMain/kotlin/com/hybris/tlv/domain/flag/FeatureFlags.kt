package com.hybris.tlv.domain.flag

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.hybris.tlv.infrastructure.platform.isDebug

internal object FeatureFlags {
    private val _flags: MutableStateFlow<Flags> = MutableStateFlow(
        value = Flags(
            devMode = isDebug,
            reset = false,
            http = true,
            networkQuality = true,
            archive = false,
            music = true
        )
    )
    val flags: Flags = _flags.asStateFlow().value

    /**
     * Sets feature flags.
     */
    fun set(flags: (Flags) -> Flags): FeatureFlags = apply {
        _flags.update { flags(it) }
    }
}
