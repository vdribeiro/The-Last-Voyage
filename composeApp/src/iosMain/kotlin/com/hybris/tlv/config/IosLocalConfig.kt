package com.hybris.tlv.config

internal class IosLocalConfig(): LocalConfig {

    override fun getBoolean(key: StorageKey): Boolean = false

    override fun getString(key: StorageKey): String = ""

    override fun getLong(key: StorageKey): Long = -1L

    override fun getDouble(key: StorageKey): Double = -1.0

    override fun put(key: StorageKey, value: Boolean?) {
    }

    override fun put(key: StorageKey, value: String?) {
    }

    override fun put(key: StorageKey, value: Long?) {
    }

    override fun put(key: StorageKey, value: Double?) {
    }
}
