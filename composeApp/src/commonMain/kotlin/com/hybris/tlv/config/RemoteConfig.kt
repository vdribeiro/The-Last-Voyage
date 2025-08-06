package com.hybris.tlv.config

internal interface RemoteConfig {

    /**
     * Set the [RemoteConfigSettings].
     */
    suspend fun settings(settings: RemoteConfigSettings): RemoteConfig

    /**
     * Set the [defaults] keys.
     */
    suspend fun setDefaults(defaults: List<StorageKey>): RemoteConfig

    /**
     * Fetch and then activate the fetched configs.
     * If the time elapsed since the last fetch from the backend is more than the default minimum fetch interval,
     * configs are fetched from the backend. After the fetch is complete, the configs are activated so that the fetched key value pairs take effect.
     *
     * Returns true if the current call activated the fetched configs;
     * false if no configs were fetched from the backend and the local fetched configs have already been activated.
     */
    suspend fun fetchAndActivate(): Boolean

    /**
     * Retrieve a boolean given the config [key].
     */
    fun getBoolean(key: StorageKey): Boolean
    /**
     * Retrieve a string given the config [key].
     */
    fun getString(key: StorageKey): String
    /**
     * Retrieve a long given the config [key].
     */
    fun getLong(key: StorageKey): Long
    /**
     * Retrieve a float given the config [key].
     */
    fun getDouble(key: StorageKey): Double
}

internal data class RemoteConfigSettings(
    /**
     * The minimum interval between successive fetch calls in seconds. Should be a non-negative number.
     */
    val minimumFetchIntervalInSeconds: Long,
    /**
     * Connection and read timeout in seconds for fetch requests. Should be a non-negative number.
     * A fetch call will fail if it takes longer than the specified timeout.
     */
    val fetchTimeoutInSeconds: Long
)
