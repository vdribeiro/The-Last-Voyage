package com.hybris.tlv.data.config

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
     * Delete configuration and preferences files and reset all caches.
     */
    suspend fun reset(): ConfigManager

    /**
     * Setup all caches.
     * Loads preferences and local configs from disk to respective caches and calls [fetchRemoteConfigs].
     */
    suspend fun setup(): ConfigManager

    /**
     * Fetch remote configs. Updates both remote and local configs if successful.
     */
    suspend fun fetchRemoteConfigs(): ConfigManager

    /**
     * Update preferences cache and save to storage.
     */
    suspend fun setPreferences(preferences: (Preferences) -> Preferences): ConfigManager

    /**
     * Update configs cache. To persist the changes, call [saveConfigs].
     */
    fun setConfigs(configs: (Configs) -> Configs): ConfigManager

    /**
     * Save configs to storage.
     */
    suspend fun saveConfigs(): ConfigManager
}
