package com.hybris.tlv.config

import kotlinx.coroutines.flow.StateFlow

/**
 * User preferences and remote configurations.
 */
internal interface ConfigManager {

    /**
     * Cached preferences.
     */
    val preferences: StateFlow<Preferences>

    /**
     * Cached local configs.
     */
    val localConfigs: Configs

    /**
     * Cached remote configs.
     */
    val remoteConfigs: Configs

    /**
     * Fetch configs and refresh all caches.
     */
    suspend fun refresh(): ConfigManager

    /**
     * Set user preferences.
     */
    suspend fun setPreferences(preferences: (Preferences) -> Preferences): ConfigManager

    /**
     * Set configs.
     */
    suspend fun setConfigs(configs: (Configs) -> Configs): ConfigManager

    /**
     * Save configs to storage.
     */
    suspend fun savePreferences(): ConfigManager

    /**
     * Save configs to storage.
     */
    suspend fun saveConfigs(): ConfigManager
}
