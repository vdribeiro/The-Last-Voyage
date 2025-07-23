package com.hybris.tlv.config

internal class CommonRemoteConfig: RemoteConfig {

    private val map = mutableMapOf<StorageKey, Any>()

    override suspend fun settings(settings: RemoteConfigSettings): RemoteConfig = this

    override suspend fun setDefaults(defaults: List<StorageKey>): RemoteConfig = apply {
        map.putAll(from = defaults.associateWith { it.defaultValue })
    }

    override suspend fun fetchAndActivate(): Boolean = true

    override fun getBoolean(key: StorageKey): Boolean =
        map.getOrElse(key = key) { key.defaultValue }.asBoolean()

    override fun getString(key: StorageKey): String =
        map.getOrElse(key = key) { key.defaultValue }.asString()

    override fun getLong(key: StorageKey): Long =
        map.getOrElse(key = key) { key.defaultValue }.asLong()

    override fun getDouble(key: StorageKey): Double =
        map.getOrElse(key = key) { key.defaultValue }.asDouble()
}
