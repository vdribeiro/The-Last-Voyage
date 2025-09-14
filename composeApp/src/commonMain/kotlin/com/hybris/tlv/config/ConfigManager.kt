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
     * Set local config.
     */
    suspend fun setLocal(configs: Configs)
}
