package com.hybris.tlv.data.config

/**
 * Manages the state and persistence of user preferences and remote-driven configurations.
 */
internal interface ConfigManager {

    /**
     * The current snapshot of user-specific settings.
     */
    val preferences: Preferences

    /**
     * The current snapshot of local configurations.
     */
    val localConfigs: Configs

    /**
     * The current snapshot of remote configurations.
     */
    val remoteConfigs: Configs

    /**
     * Wipes all persisted configuration and preference files from the device and resets in-memory caches to their defaults.
     *
     * @return The [ConfigManager] instance for chaining.
     */
    suspend fun reset(): ConfigManager

    /**
     * Bootstraps the manager by loading data from disk into memory and triggering an initial remote sync.
     *
     * @return The [ConfigManager] instance for chaining.
     */
    suspend fun setup(): ConfigManager

    /**
     * Updates the in-memory [preferences] cache.
     * This change is volatile and will be lost unless [savePreferences] is called subsequently.
     *
     * @param preferences A lambda that takes the current [Preferences] and returns the updated state.
     * @return The [ConfigManager] instance for chaining.
     */
    suspend fun setPreferences(preferences: (Preferences) -> Preferences): ConfigManager

    /**
     * Persists the current in-memory [preferences] to the device's storage.
     *
     * @return The [ConfigManager] instance for chaining.
     */
    suspend fun savePreferences(): ConfigManager

    /**
     * Updates the in-memory [localConfigs] cache.
     * This change is volatile and will be lost unless [saveConfigs] is called subsequently.
     *
     * @param configs A lambda that takes the current [Configs] and returns the updated state.
     * @return The [ConfigManager] instance for chaining.
     */
    fun setConfigs(configs: (Configs) -> Configs): ConfigManager

    /**
     * Persists the current in-memory [localConfigs] to the device's storage.
     *
     * @return The [ConfigManager] instance for chaining.
     */
    suspend fun saveConfigs(): ConfigManager
}
