package com.hybris.tlv.storage

internal interface StorageManager {

    /**
     * Cached local config.
     */
    val config: Config

    /**
     * Get local config.
     */
    suspend fun getLocal(): Config?

    /**
     * Get remote config.
     */
    suspend fun getRemote(): Config?

    /**
     * Set local config.
     */
    suspend fun setLocal(config: Config)
}
