package com.hybris.tlv.config

// TODO - Firebase RemoteConfig
internal class IosRemoteConfig: RemoteConfig {

    private val map = mutableMapOf<Config, Any>()

    override suspend fun settings(settings: RemoteConfigSettings): RemoteConfig = this

    override suspend fun setDefaults(defaults: List<Config>): RemoteConfig = apply {
        map.putAll(from = defaults.associateWith { it.defaultValue })
    }

    override suspend fun fetchAndActivate(): Boolean = true

    override fun getBoolean(key: Config): Boolean =
        map.getOrElse(key = key) { key.defaultValue }.asBoolean()

    override fun getString(key: Config): String =
        map.getOrElse(key = key) { key.defaultValue }.asString()

    override fun getLong(key: Config): Long =
        map.getOrElse(key = key) { key.defaultValue }.asLong()

    override fun getDouble(key: Config): Double =
        map.getOrElse(key = key) { key.defaultValue }.asDouble()
}
