package com.hybris.tlv.config

internal class IosRemoteConfig: RemoteConfig {

    override suspend fun settings(settings: RemoteConfigSettings): RemoteConfig = this

    override suspend fun setDefaults(defaults: List<StorageKey>): RemoteConfig = this

    override suspend fun fetchAndActivate(): Boolean = false

    override fun getBoolean(key: StorageKey): Boolean = false

    override fun getString(key: StorageKey): String = ""

    override fun getLong(key: StorageKey): Long = -1L

    override fun getDouble(key: StorageKey): Double = -1.0
}
