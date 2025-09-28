package com.hybris.tlv.config

/**
 * User preferences and remote configurations.
 */
internal interface ConfigManager {

    /**
     * Cached local configs.
     */
    var localConfigs: Configs

    /**
     * Cached remote configs.
     */
    val remoteConfigs: Configs

    /**
     * Fetch configs and refresh caches.
     */
    suspend fun fetch()

    /**
     * Set configs to storage.
     */
    suspend fun flush(configs: Configs = localConfigs)

    /**
     * Get user preferences.
     */
    suspend fun getPreferences(): Preferences

    /**
     * Set user preferences.
     */
    suspend fun setPreferences(preferences: (Preferences) -> Preferences): Boolean
}
