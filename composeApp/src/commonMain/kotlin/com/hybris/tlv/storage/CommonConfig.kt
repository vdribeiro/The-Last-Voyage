package com.hybris.tlv.storage

internal class CommonConfig: Config {

    private val map = mutableMapOf<ConfigKey, Any>()

    override fun getBoolean(key: ConfigKey): Boolean =
        map.getOrElse(key = key) { key.defaultValue }.asBoolean()

    override fun getString(key: ConfigKey): String =
        map.getOrElse(key = key) { key.defaultValue }.asString()

    override fun getLong(key: ConfigKey): Long =
        map.getOrElse(key = key) { key.defaultValue }.asLong()

    override fun getDouble(key: ConfigKey): Double =
        map.getOrElse(key = key) { key.defaultValue }.asDouble()

    override fun put(key: ConfigKey, value: Boolean?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: ConfigKey, value: String?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: ConfigKey, value: Long?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: ConfigKey, value: Double?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }
}
