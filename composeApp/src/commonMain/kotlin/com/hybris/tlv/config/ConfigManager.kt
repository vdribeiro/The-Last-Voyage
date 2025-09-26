package com.hybris.tlv.config

internal interface ConfigManager {

    /**
     * Cached local config.
     */
    var localConfigs: Configs

    /**
     * Cached remote config.
     */
    val remoteConfigs: Configs

    /**
     * Fetch configs and refresh caches.
     */
    suspend fun fetch()

    /**
     * Set config to storage.
     */
    suspend fun flush(configs: Configs = localConfigs)
}
