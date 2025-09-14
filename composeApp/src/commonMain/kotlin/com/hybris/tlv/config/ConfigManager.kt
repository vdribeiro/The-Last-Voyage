package com.hybris.tlv.config

internal interface ConfigManager {

    /**
     * Cached local config.
     */
    val localConfigs: Configs

    /**
     * Cached remote config.
     */
    val remoteConfigs: Configs

    /**
     * Get local config.
     */
    suspend fun fetchLocal()

    /**
     * Get remote config.
     */
    suspend fun fetchRemote()

    /**
     * Set local config.
     */
    suspend fun setLocal(configs: Configs)
}
