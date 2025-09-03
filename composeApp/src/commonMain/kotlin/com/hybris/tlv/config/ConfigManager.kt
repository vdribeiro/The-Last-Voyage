package com.hybris.tlv.config

internal interface ConfigManager {

    /**
     * If the fetch service is enabled or not.
     */
    var enabled: Boolean

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
