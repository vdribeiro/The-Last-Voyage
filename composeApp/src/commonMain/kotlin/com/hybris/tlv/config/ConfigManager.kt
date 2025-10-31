package com.hybris.tlv.config

/**
 * User preferences and remote configurations.
 */
internal interface ConfigManager {

    /**
     * Cached preferences.
     */
    val preferences: Preferences

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
    suspend fun refresh()

    /**
     * Set user preferences.
     */
    suspend fun setPreferences(preferences: (Preferences) -> Preferences)

    /**
     * Set configs.
     */
    suspend fun setConfigs(configs: (Configs) -> Configs)

    /**
     * Save configs to storage.
     */
    suspend fun savePreferences()

    /**
     * Save configs to storage.
     */
    suspend fun saveConfigs()

    /**
     * Delete user preferences and configs.
     */
    fun reset()
}
