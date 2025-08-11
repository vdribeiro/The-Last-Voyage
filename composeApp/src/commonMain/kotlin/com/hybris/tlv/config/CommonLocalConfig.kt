package com.hybris.tlv.config

internal class CommonLocalConfig: LocalConfig {

    private val map = mutableMapOf<Config, Any>()

    override fun getBoolean(key: Config): Boolean =
        map.getOrElse(key = key) { key.defaultValue }.asBoolean()

    override fun getString(key: Config): String =
        map.getOrElse(key = key) { key.defaultValue }.asString()

    override fun getLong(key: Config): Long =
        map.getOrElse(key = key) { key.defaultValue }.asLong()

    override fun getDouble(key: Config): Double =
        map.getOrElse(key = key) { key.defaultValue }.asDouble()

    override fun put(key: Config, value: Boolean?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: Config, value: String?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: Config, value: Long?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }

    override fun put(key: Config, value: Double?) {
        if (value == null) map.remove(key = key) else map.put(key = key, value = value)
    }
}
