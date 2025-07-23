package com.hybris.tlv.config

internal class CommonLocalConfig: LocalConfig {

    private val map = mutableMapOf<StorageKey, Any>()

    override fun getBoolean(key: StorageKey): Boolean =
        map.getOrElse(key = key) { key.defaultValue }.asBoolean()

    override fun getString(key: StorageKey): String =
        map.getOrElse(key = key) { key.defaultValue }.asString()

    override fun getLong(key: StorageKey): Long =
        map.getOrElse(key = key) { key.defaultValue }.asLong()

    override fun getDouble(key: StorageKey): Double =
        map.getOrElse(key = key) { key.defaultValue }.asDouble()

    override fun put(key: StorageKey, value: Boolean?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: StorageKey, value: String?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: StorageKey, value: Long?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: StorageKey, value: Double?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }
}
