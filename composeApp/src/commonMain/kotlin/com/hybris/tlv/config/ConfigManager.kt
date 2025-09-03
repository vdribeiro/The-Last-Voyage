package com.hybris.tlv.config

internal interface ConfigManager {

    /**
     * Cached local config.
     */
    val configs: Configs

    /**
     * Get local config.
     */
    suspend fun getLocal(): Configs

    /**
     * Get remote config.
     */
    suspend fun getRemote(): Configs

    /**
     * Set local config.
     */
    suspend fun setLocal(configs: Configs)
}
