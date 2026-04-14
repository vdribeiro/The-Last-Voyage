package com.hybris.tlv.domain.flag

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

/**
 * The central coordinator for feature toggles and environment settings.
 *
 * This singleton maintains the current [Flags] state and allows for dynamic enabling/disabling of core application features
 * without requiring a restart or a separate build flavor.
 *
 * ### Architectural Role:
 * - **Decoupling:** Modules consume flags to decide which execution paths to take.
 * - **Reactivity:** By using [MutableStateFlow], any part of the application can observe changes to these flags to update the logic immediately.
 */
internal object FeatureFlags {

    /**
     * The backing state for feature toggles.
     * Initialized with all features disabled by default to ensure a safe baseline.
     */
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

    /**
     * Returns the current snapshot of the feature [Flags].
     * This provides a non-reactive, thread-safe way to check the current status of a feature.
     */
    val flags: Flags get() = _flags.value

    /**
     * Atomically updates the current feature flags and returns the new state.
     *
     * @param flags A transform function that receives the current [Flags] and returns a modified copy.
     * @return The updated [Flags] instance.
     */
    fun set(flags: (Flags) -> Flags): Flags = _flags.updateAndGet(function = flags)
}