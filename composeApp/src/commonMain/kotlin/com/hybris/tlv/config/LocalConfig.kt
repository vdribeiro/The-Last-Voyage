package com.hybris.tlv.config

internal interface LocalConfig {

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

    /**
     * Set a boolean [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: StorageKey, value: Boolean? = null)
    /**
     * Set a string [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: StorageKey, value: String? = null)
    /**
     * Set a long [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: StorageKey, value: Long? = null)
    /**
     * Set a double [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: StorageKey, value: Double? = null)
}
