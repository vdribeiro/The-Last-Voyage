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
         * Set user preferences. If [save] is true, then save immediately to storage.
     */
    suspend fun setPreferences(save: Boolean = false, preferences: (Preferences) -> Preferences)

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
