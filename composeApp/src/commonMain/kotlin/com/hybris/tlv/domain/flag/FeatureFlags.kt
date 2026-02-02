package com.hybris.tlv.domain.flag

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

internal object FeatureFlags {
    private val _flags: MutableStateFlow<Flags> = MutableStateFlow(value = Flags())
    val flags: Flags get() = _flags.value

    /**
     * Sets feature flags.
     */
    fun set(flags: (Flags) -> Flags): Flags = _flags.updateAndGet(function = flags)
}
