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
    val localConfigs: StateFlow<Configs>

    /**
     * Cached remote configs.
     */
    val remoteConfigs: StateFlow<Configs>

    /**
     * Setup all caches.
     */
    suspend fun setup(): ConfigManager

    /**
     * Fetch remote configs.
     */
    suspend fun fetchRemoteConfigs(): ConfigManager

    /**
     * Update preferences cache and save to storage.
     */
    suspend fun setPreferences(preferences: (Preferences) -> Preferences): ConfigManager

    /**
     * Update configs cache.
     */
    suspend fun setConfigs(configs: (Configs) -> Configs): ConfigManager

    /**
     * Save configs to storage.
     */
    suspend fun saveConfigs(): ConfigManager
}
