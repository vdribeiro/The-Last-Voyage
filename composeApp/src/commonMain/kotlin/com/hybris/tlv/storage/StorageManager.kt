package com.hybris.tlv.storage

internal interface StorageManager {

    /**
     * Retrieve config.
     */
    fun get(): Config

    /**
     * Set a string [value] into the config given its [key] or use null to delete it.
     */
    fun set(key: Config)
}
