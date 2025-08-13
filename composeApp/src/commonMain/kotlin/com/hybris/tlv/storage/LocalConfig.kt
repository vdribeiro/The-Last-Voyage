package com.hybris.tlv.storage

internal interface LocalConfig {

    /**
     * Retrieve a string given the config [key].
     */
    fun getString(key: Config): String
    /**
     * Retrieve a boolean given the config [key].
     */
    fun getBoolean(key: Config): Boolean
    /**
     * Retrieve a long given the config [key].
     */
    fun getLong(key: Config): Long
    /**
     * Retrieve a float given the config [key].
     */
    fun getDouble(key: Config): Double

    /**
     * Set a string [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: Config, value: String? = null)
    /**
     * Set a boolean [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: Config, value: Boolean? = null)
    /**
     * Set a long [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: Config, value: Long? = null)
    /**
     * Set a double [value] into the config given its [key] or use null to delete it.
     */
    fun put(key: Config, value: Double? = null)
}
