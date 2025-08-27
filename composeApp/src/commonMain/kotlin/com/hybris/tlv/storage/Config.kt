package com.hybris.tlv.storage

/**
 * Local configurations.
 */
internal interface Config {

    /**
     * Retrieve a string given the config [key].
     */
    fun getString(key: ConfigKey): String
    /**
     * Retrieve a boolean given the config [key].
     */
    fun getBoolean(key: ConfigKey): Boolean
    /**
     * Retrieve a long given the config [key].
     */
    fun getLong(key: ConfigKey): Long
    /**
     * Retrieve a float given the config [key].
     */
    fun getDouble(key: ConfigKey): Double

    /**
     * Set a string [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: ConfigKey, value: String? = null)
    /**
     * Set a boolean [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: ConfigKey, value: Boolean? = null)
    /**
     * Set a long [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: ConfigKey, value: Long? = null)
    /**
     * Set a double [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: ConfigKey, value: Double? = null)
}
