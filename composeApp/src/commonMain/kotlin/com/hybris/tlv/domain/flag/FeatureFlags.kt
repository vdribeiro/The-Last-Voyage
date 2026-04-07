package com.hybris.tlv.domain.flag

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

/**
 * Manages feature flags.
 */
internal object FeatureFlags {
    private val _flags: MutableStateFlow<Flags> = MutableStateFlow(
        value = Flags(
            devMode = false,
            console = false,
            reset = false,
            http = false,
            archive = false,
            music = false,
            engines = false
        )
    )
    val flags: Flags get() = _flags.value

    /**
     * Sets feature flags.
     */
    fun set(flags: (Flags) -> Flags): Flags = _flags.updateAndGet(function = flags)
}
